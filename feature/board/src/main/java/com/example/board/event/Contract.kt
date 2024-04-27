package com.example.board.event

sealed interface Contract {

    sealed interface Event {
        data class OnClickPagingItem(val id: String, val mediaUrl: String) : Event
    }

    sealed interface Effect {
        data class NavigateBoardDetailFragment(val id: String, val mediaUrl: String) : Effect
    }

}