package com.iie.opsc7312_regularbirds

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import java.time.LocalDate
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AddNewObservationFragment : Fragment() {

    private var mStorageRef: StorageReference? = null
    private var mUploadTask: StorageTask<*>? = null
    private lateinit var imageView: ImageView
    var imageUploadURL : String =""
    private var imgUri: Uri? = null;
    private lateinit var firebaseFirestore: FirebaseFirestore
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var id:String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        id =BirdObservationHandler.generateObservationId()
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_add_new_observation, container, false)

        var txtBirdName = view.findViewById<TextView>(R.id.txtBirdName)
        var btnCreateObsrevation = view.findViewById<Button>(R.id.btnCreateNew)
        var btnClear = view.findViewById<Button>(R.id.btnClear)
        val addPicbutton: Button = view.findViewById<Button>(R.id.btnAddPicture)
        imageView = view.findViewById(R.id.imgEntryImage)
        initVars()


        mStorageRef = FirebaseStorage.getInstance().getReference("entryImages");
        Firebase.initialize(context = requireContext())
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
        btnClear.setOnClickListener {
            txtBirdName.setText("")
            imageView.setImageDrawable(null)
        }
        btnCreateObsrevation.setOnClickListener {


            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("User Location")
                .setMessage("Do you want to save this observation? Your current location will be saved to.")
            builder.setPositiveButton("OK") { dialog, which ->
                if (txtBirdName != null) {
                    if (imgUri != null) {
                        uploadImage()
                        imageUploadURL = "HasImage"
                    }
                    var newObserv = BirdObservationModel(
                        observationId = id,
                        observationName = "Observation " + txtBirdName.text.toString(),
                        observationDate = LocalDate.now().toString(),
                        userLocationLongitude = BirdHotspots.userLongitude,
                        userLocationLatitude = BirdHotspots.userLatitude,
                        UserId = UserHandler.getVerifiedUser()!!,
                        imageData = imageUploadURL

                    )
                    BirdObservationHandler.addDataCObservationToFirestore(newObserv)


                    lifecycleScope.launch {

                        val job1 =
                            async { BirdObservationHandler.getUserObservationsFromFireStore() }
                        val job2 = async { BirdObservationHandler.getImagesFromFireStore() }

                        try {
                            // Await the completion of all jobs
                            job1.await()
                            job2.await()
                        } catch (e: Exception) {
                            // Handle the exception
                        }
                    }

                }

                Toast.makeText(requireContext(), "Data added successfully", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                Toast.makeText(requireContext(), "Data Not Saved", Toast.LENGTH_SHORT).show()
            }
            val dialog = builder.create()
            dialog.show()


        }

        addPicbutton.setOnClickListener {

            // start your next activity
            //startActivity(intent)
            pickImageFromGallery()
        }
        return view
    }


     fun uploadImage() {
         val progressDialog = ProgressDialog(requireContext())
         progressDialog.setTitle("Uploading...")
         progressDialog.show()
        mStorageRef = mStorageRef?.child(System.currentTimeMillis().toString())
        imgUri?.let {
            mStorageRef?.putFile(it)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Task", "Success " + task.result.toString())
                    mStorageRef!!.downloadUrl.addOnSuccessListener { uri ->

                        val entryImage = entryImages(
                            uri.toString(),
                            UserHandler.getVerifiedUser()!!,
                            id
                        )

                        Log.d("Image  Url Download", uri.toString())

                        firebaseFirestore.collection("entryImages").add(entryImage)
                            .addOnCompleteListener { firestoreTask ->

                                if (firestoreTask.isSuccessful) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Uploaded Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("FireStore", firestoreTask.toString())
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        firestoreTask.exception?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        // Hide the loading spinner
                        progressDialog.dismiss()
                    }
                } else {
                    Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT)
                        .show()
                    // Hide the loading spinner
                    progressDialog.dismiss()
                }
            }
        }

    }

    private fun initVars() {

        mStorageRef = FirebaseStorage.getInstance().reference.child("entryImages")
        firebaseFirestore = Firebase.firestore
    }


     fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.getData() != null
        ) {
            val imageUri = imageView.setImageURI(data?.data)
            imgUri = data?.data
            Picasso.get().load(imgUri).into(imageView)

        }
    }
}