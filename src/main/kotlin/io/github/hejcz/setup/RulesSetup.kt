package io.github.hejcz.setup

import io.github.hejcz.basic.*
import io.github.hejcz.core.EndRule
import io.github.hejcz.core.Rule

class RulesSetup {
    private var rules = BASIC_RULES

    private var endRules = BASIC_END_RULES

    fun add(rule: Rule) {
        rules = rules + rule
    }

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

    companion object {
        private val BASIC_RULES: List<Rule> = listOf(CastleCompletedRule, RoadCompletedRule, CloisterCompletedRule)

        private val BASIC_END_RULES: List<EndRule> =
            listOf(IncompleteCastleRule, IncompleteRoadRule, IncompleteCloisterRule, RewardGreenFieldsRule)
    }
}
