package fr.florianvansteene.androidgraphql.ListViewModels

/**
 * Created by florianvansteene on 09/04/2018.
 */
class LinksModel {
    var url: String = ""
    var description: String = ""

    constructor() {}

    constructor(url: String, description: String) {
        this.url = url
        this.description = description
    }
}