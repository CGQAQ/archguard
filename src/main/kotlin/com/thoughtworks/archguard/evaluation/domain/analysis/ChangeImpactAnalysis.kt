package com.thoughtworks.archguard.evaluation.domain.analysis

import com.thoughtworks.archguard.evaluation.domain.analysis.report.Report
import org.springframework.stereotype.Service

@Service
class ChangeImpactAnalysis : Analysis {
    override fun getQualityReport(): Report {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        return "变更影响"
    }

}
