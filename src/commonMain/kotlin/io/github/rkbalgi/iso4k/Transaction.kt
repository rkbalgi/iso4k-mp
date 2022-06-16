package io.github.rkbalgi.iso4k

class Transaction(private val req: Message) {


    private var response: Message? = null


    fun request(): Message {
        return req
    }

    fun response(): Message? {
        return response
    }
}