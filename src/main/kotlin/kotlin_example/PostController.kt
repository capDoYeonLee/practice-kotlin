package kotlin_example

import org.springframework.web.bind.annotation.*

@RestController

class PostController(
    private val postService: PostService
) {
    @GetMapping
    fun getPosts(): List<Post> = postService.findAll()

    @GetMapping("/{id}")
    fun getPost(@PathVariable id: Long): Post = postService.findPostById(id)

    @PostMapping
    fun createPost(@RequestBody post: PostDto) = postService.save(post)

    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id: Long) = postService.deleteById(id)

    @PostMapping("/user")
    fun addUser(@RequestBody userDto: UserDto) = postService.saveUser(userDto)
}
