package org.duhan.commerce.core.domain

import org.duhan.commerce.core.support.OffsetLimit
import org.duhan.commerce.core.support.Page
import org.duhan.commerce.core.support.error.CoreException
import org.duhan.commerce.core.support.error.ErrorType
import org.duhan.commerce.storage.db.core.SampleEntity
import org.duhan.commerce.storage.db.core.SampleRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class SampleService(
    private val sampleRepository: SampleRepository,
) {
    fun findSamples(offsetLimit: OffsetLimit): Page<Sample> {
        val samples = sampleRepository.findAll(offsetLimit.toPageable())
        return Page(samples.content.map { Sample(it.name) }, samples.hasNext())
    }

    fun findSample(sampleId: Long): Sample {
        val found = sampleRepository.findByIdOrNull(sampleId)
            ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        return Sample(
            name = found.name,
        )
    }

    fun createSample(sample: Sample): Long {
        val savedSample = sampleRepository.save(
            SampleEntity(
                name = sample.name,
            ),
        )
        return savedSample.id
    }
}
