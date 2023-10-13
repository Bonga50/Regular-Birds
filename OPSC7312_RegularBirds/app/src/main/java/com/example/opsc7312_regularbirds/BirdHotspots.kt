package com.example.opsc7312_regularbirds

object BirdHotspots {
    lateinit var  selectedBirdHotspot : BirdHotspotModel
    val birdHotspotList = listOf(
        BirdHotspotModel("1", "American Robin", "Turdus migratorius", -77.0369, 38.9072, "Washington, D.C."),
        BirdHotspotModel("2", "Mallard", "Anas platyrhynchos", -122.4194, 37.7749, "San Francisco, CA"),
        BirdHotspotModel("3", "Northern Cardinal", "Cardinalis cardinalis", -84.3879, 33.7490, "Atlanta, GA"),
        BirdHotspotModel("4", "Black-capped Chickadee", "Poecile atricapillus", -71.0589, 42.3601, "Boston, MA"),
        BirdHotspotModel("5", "Bald Eagle", "Haliaeetus leucocephalus", -77.0369, 38.9072, "Washington, D.C.")
    )

    fun setSelectedhotSpotData(locId:String){
        selectedBirdHotspot= birdHotspotList.filter {it.locId.equals(locId)}.first();
    }

    fun getSelectedHotSpotData():BirdHotspotModel{
        return selectedBirdHotspot
    }

    fun getTop100HotspotData(): List<BirdHotspotModel>{
        var tempRecent: List<BirdHotspotModel>

        tempRecent = birdHotspotList
        if (tempRecent.size < 30) {
            return tempRecent
        } else {
            return tempRecent.take(30)
        }
    }

}
