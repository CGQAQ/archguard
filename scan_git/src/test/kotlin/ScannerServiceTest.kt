// todo: why can not use package base path

import com.thoughtworks.archguard.git.scanner.Config
import com.thoughtworks.archguard.git.scanner.Main
import com.thoughtworks.archguard.git.scanner.ScannerService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Main::class])
internal class ScannerServiceTest(
        @Autowired val service: ScannerService) {

    @Test
    internal fun `to scan specified git repository`() {
        service.scan(Config("/Users/ygdong/Downloads/gittest"))
    }

    @Test
    internal fun `get commit history`() {
        val findAll = service.findAll()
        assertAll("",
                { assertEquals(1, findAll.count()) },
                { assertEquals("master", findAll.first().branch) })
    }
}