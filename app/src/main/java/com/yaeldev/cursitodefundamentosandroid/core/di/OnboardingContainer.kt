package com.yaeldev.cursitodefundamentosandroid.core.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.domain.repositories.OnboardingPreferences
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.domain.usecases.MarcarOnboardingCompletadoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.domain.usecases.OnboardingCompletadoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.presentation.onboarding.OnboardingViewModel
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.presentation.splash.SplashViewModel

/**
 * DI del feature onboarding. Recibe la preferencia local (instancia unica) y expone
 * una factory compartida con un initializer por ViewModel (splash y onboarding).
 */
class OnboardingContainer(
    private val onboardingPreferences: OnboardingPreferences
) {
    val factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { SplashViewModel(OnboardingCompletadoUseCase(onboardingPreferences)) }
        initializer { OnboardingViewModel(MarcarOnboardingCompletadoUseCase(onboardingPreferences)) }
    }
}
