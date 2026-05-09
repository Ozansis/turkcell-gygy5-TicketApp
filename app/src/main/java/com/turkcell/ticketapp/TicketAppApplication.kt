package com.turkcell.ticketapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


//uygulama başlayınca aktiviteden önce oluşur
//Singleton
//Uygulama kapanana kadar kapanmaz
class TicketAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TicketAppApplication)  //Koin appcontexte ihtiyaç duyar . Bazı bağımlılıklar contexte ihtiyaç duyar. Kullanabilsin diye veriririz.
            modules(

            )
        }
    }
}