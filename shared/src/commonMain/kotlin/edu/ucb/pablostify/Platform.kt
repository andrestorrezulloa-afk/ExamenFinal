package edu.ucb.pablostify

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform