package io.github.hejcz.engine

import io.github.hejcz.api.State
import io.github.hejcz.components.buildersandtraders.BuildersAndTradersExtension
import io.github.hejcz.components.buildersandtraders.BuildersAndTradersStateExtensionApi
import io.github.hejcz.components.magic.MageAndWitchState

fun State.getWizardState() = this.get(MageAndWitchState.ID)?.let { it as MageAndWitchState }

fun State.getBuilderState() = this.get(BuildersAndTradersExtension.ID)?.let { it as BuildersAndTradersStateExtensionApi }