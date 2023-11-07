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

    var EntryId: Int
        get() = _EntryId
        set(value) {
            _EntryId = value
        }

    private var _ImageUrl: String = ""
    private var _userId: String = ""
    private var _EntryId: Int = 0

    constructor()

    constructor(
        ImageUrl: String,
        userId: String,
        EntryId: Int
    ) {
        this.ImageUrl = ImageUrl
        this.userId = userId
        this.EntryId = EntryId
    }
}