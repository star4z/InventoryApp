package edu.sunypoly.inventoryapp

class AsyncGetItems(private val listener: AsyncEventListener) {
    val items = ArrayList<InventoryItem>()

    fun doTheThing() {
        listener.onEventStart()

        listener.onEventFinish()

    }
}