package com.measify.kappmaker.data.source.remote.request.ai.replicate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a request to create a prediction on a Replicate model.
 * Some models require version of the model to be specified, while others don't.
 */

@Serializable
data class ReplicatePredictionRequest<Input>(
    @SerialName("version") val version: String = "",
    @SerialName("input") val input: Input,
)