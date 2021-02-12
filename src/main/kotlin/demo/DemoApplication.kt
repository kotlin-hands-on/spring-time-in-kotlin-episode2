package demo

import demo.kx.uuid
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.query
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@Controller
class HelloController {

    @GetMapping("/hello")
    fun index(model: Model): String {
        // https://docs.spring.io/spring-framework/docs/current/kdoc-api/spring-framework/org.springframework.ui/index.html
        model["message"] = "Hello"
        return "greeting"
    }
}

@RestController
class MessageResource(val service: MessageService) {
    @GetMapping
    fun index(): List<Message> = service.findMessages()

    @GetMapping("/{id}")
    fun index(@PathVariable id: String): List<Message> = service.findMessageById(id)

    @PostMapping
    fun post(@RequestBody message: Message) {
        service.post(message)
    }

}

@Service
class MessageService(val db: JdbcTemplate) {

    fun findMessages(): List<Message> = db.query("select * from messages") { rs, _ ->
        Message(rs.getString("id"), rs.getString("text"))
    }

    fun findMessageById(id: String): List<Message> = db.query("select * from messages where id = ?", id) { rs, _ ->
        Message(rs.getString("id"), rs.getString("text"))
    }

    fun post(message: Message){
        db.update("insert into messages values ( ?, ? )", message.id ?: message.text.uuid(),
            message.text)
    }
}

data class Message(val id: String?, val text: String)

