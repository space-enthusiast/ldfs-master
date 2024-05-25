import com.ldfs.control.domain.model.entity.ChunkServerEntity
import com.ldfs.control.domain.service.ChunkServerAccessService
import com.ldfs.main.dto.request.HeartBeatRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chunkServer")
class ChunkServerController @Autowired constructor(private val chunkServerAccessService: ChunkServerAccessService) {

    @GetMapping
    fun chunkServers(): ResponseEntity<List<ChunkServerEntity>> {
        return ResponseEntity.ok().body(chunkServerAccessService.findAll())
    }

    @PostMapping("/heartBeat")
    fun heartBeatUpdateServerStatus(@RequestBody heartBeatRequest: HeartBeatRequest): ResponseEntity<ChunkServerEntity> {
        val storedEntity = chunkServerAccessService.makeServerDiscoverable(
            heartBeatRequest.ip!!,
            heartBeatRequest.port!!,
            heartBeatRequest.remainingStorageSize!!
        )
        return ResponseEntity.ok().body(storedEntity)
    }
}