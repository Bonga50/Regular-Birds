package com.example.opsc7312_regularbirds

class entryImages {
    var ImageUrl: String
        get() = _ImageUrl
        set(value) {
            _ImageUrl = value
        }

    var userId: String
        get() = _userId
        set(value) {
            _userId = value
        }

    var EntryId: String
        get() = _EntryId
        set(value) {
            _EntryId = value
        }

    private var _ImageUrl: String = ""
    private var _userId: String = ""
    private var _EntryId: String = ""

    constructor()

    constructor(
        ImageUrl: String,
        userId: String,
        EntryId: String
    ) {
        this.ImageUrl = ImageUrl
        this.userId = userId
        this.EntryId = EntryId
    }
}