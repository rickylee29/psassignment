package com.example.psassignment.ui.screens.drivers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.psassignment.domain.model.Assignment
import com.example.psassignment.domain.usecase.CalculateOptimizedAssignmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriversViewModel @Inject constructor(
    private val calculateAssignmentsUseCase: CalculateOptimizedAssignmentsUseCase
) : ViewModel() {

    // UI State definition
    sealed class UiState {
        object Loading : UiState()
        data class Success(val assignments: List<Assignment>) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _selectedAssignment = MutableStateFlow<Assignment?>(null)
    val selectedAssignment: StateFlow<Assignment?> = _selectedAssignment.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                // The algorithm runs here on the background thread via the UseCase
                val result = calculateAssignmentsUseCase()
                _uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to calculate routes: ${e.message}")
            }
        }
    }

    fun onDriverSelected(assignment: Assignment) {
        _selectedAssignment.value = assignment
    }

    fun onDismissDialog() {
        _selectedAssignment.value = null
    }
}