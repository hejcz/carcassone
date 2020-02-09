package io.github.hejcz.setup

import io.github.hejcz.core.*
import io.github.hejcz.core.rule.*
import kotlin.reflect.*

class RulesSetup {
    private var rules = BASIC_RULES

    private var endRules = BASIC_END_RULES

    fun add(vararg newRules: Rule) {
        rules = rules + newRules
    }

    fun add(endRule: EndRule) {
        endRules = endRules + endRule
    }

    fun withExtensions(vararg extensions: Extension): RulesSetup {
        extensions.forEach { it.modify(this) }
        return this
    }

    fun rules(): Collection<Rule> = rules

    fun endRules(): Collection<EndRule> = endRules

    fun <T : Any> replace(replaced: KClass<T>, replacement: Rule) {
        rules = rules.filter { it::class != replaced } + replacement
    }

    fun <T : Any> replace(replaced: KClass<T>, replacement: EndRule) {
        endRules = endRules.filterNot { it::class == replaced } + replacement
    }

    companion object {
        private val BASIC_RULES: List<Rule> =
            listOf(RewardCompletedCastle(castleScoring), RewardCompletedRoad(roadScoring), RewardCompletedCloister)

        private val BASIC_END_RULES: List<EndRule> =
            listOf(
                RewardIncompleteCastle(incompleteCastleScoring),
                RewardIncompleteRoad(roadScoring),
                RewardIncompleteCloister,
                RewardPeasants
            )
    }
}
