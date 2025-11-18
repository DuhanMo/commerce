package org.duhan.commerce.storage.db.core

import org.assertj.core.api.Assertions.assertThat
import org.duhan.commerce.core.enums.SampleState
import org.duhan.commerce.storage.db.CoreDbContextTest
import org.junit.jupiter.api.Test

class SampleRepositoryTest(
    private val sampleRepository: SampleRepository,
) : CoreDbContextTest() {

    @Test
    fun `샘플 엔티티가 저장된다`() {
        // given
        sampleRepository.save(SampleEntity("샘플", SampleState.SELLING))

        // when
        val sample = sampleRepository.findAll().first()

        // then
        assertThat(sample.name).isEqualTo("샘플")
    }
}
