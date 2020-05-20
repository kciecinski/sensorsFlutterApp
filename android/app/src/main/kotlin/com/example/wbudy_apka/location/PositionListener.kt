package com.example.wbudy_apka.location

import com.example.wbudy_apka.location.Position

interface PositionListener {
    fun newPosition(position: Position)
}