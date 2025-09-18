package kotlin_example

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService
) {
    @GetMapping
    fun getPosts(): List<Post> = postService.findAll()

    @GetMapping("/{id}")
    fun getPost(@PathVariable id: Long): Post = postService.findById(id)

    @PostMapping
    fun createPost(@RequestBody post: Post): Post = postService.save(post)

    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id: Long) = postService.deleteById(id)
}
