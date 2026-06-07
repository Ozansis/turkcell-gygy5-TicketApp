package com.turkcell.ticketapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.turkcell.core.domain.auth.AuthRepository
import com.turkcell.core.domain.auth.UserRole
import com.turkcell.ticketapp.screen.EventDetailScreen
import com.turkcell.ticketapp.screen.HomeScreen
import com.turkcell.ticketapp.screen.LoginScreen
import com.turkcell.ticketapp.screen.RegisterScreen
import com.turkcell.ticketapp.screen.StaffScreen
import com.turkcell.ticketapp.screen.TicketDetailScreen
import org.koin.compose.koinInject

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    authRepository: AuthRepository = koinInject()
) {
    val isLoggedIn by authRepository.isLoggedIn.collectAsStateWithLifecycle(initialValue = null)
    val currentRole by authRepository.currentRole.collectAsStateWithLifecycle(initialValue = UserRole.USER)

    when (isLoggedIn) {
        null  -> SplashScreen()
        false -> UnAuthedNavHost(navController)
        true  -> when (currentRole) {
            UserRole.STAFF, UserRole.ADMIN -> StaffNavHost(navController)
            UserRole.USER                  -> AuthedNavHost(navController)
        }
    }
}

@Composable
private fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AuthedNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(
                onEventClick = { id -> navController.navigate(EventDetail(id)) },
                onTicketClick = { id -> navController.navigate(TicketDetail(id)) }
            )
        }
        composable<TicketDetail> {
            val route = it.toRoute<TicketDetail>()
            TicketDetailScreen(
                id = route.id,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable<EventDetail> {
            val route = it.toRoute<EventDetail>()
            EventDetailScreen(
                id = route.id,
                onBackClick = { navController.popBackStack() },
                onPurchaseSuccess = { navController.navigate(Home) }
            )
        }
    }
}


@Composable
private fun StaffNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Staff) {
        composable<Staff> {
            StaffScreen()
        }
    }
}

@Composable
private fun UnAuthedNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Home) },
                onNavigateToRegister = { navController.navigate(Register) }
            )
        }
        composable<Register> {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate(Login) },
                onRegisterSuccess = { navController.navigate(Home) }
            )
        }
    }
}