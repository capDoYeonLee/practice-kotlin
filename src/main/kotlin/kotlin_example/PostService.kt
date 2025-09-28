package kotlin_example

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    fun findAll(): List<Post> = postRepository.findAll()

    fun findPostById(id: Long): Post =
        postRepository.findById(id).orElseThrow { IllegalArgumentException("Post not found") }

    @Transactional
    fun save(dto: PostDto){
        val user = userRepository.findById(dto.userId).orElseThrow { IllegalArgumentException("Post not found") }
        val post: Post = Post(
            title = dto.title,
            content = dto.content,
            author = user
        )
        postRepository.save(post)
    }

    @Transactional
    fun saveUser(userDto: UserDto) {
        val user:  User = User(
            name = userDto.name
        )
        userRepository.save(user)
    }

    fun deleteById(id: Long) = postRepository.deleteById(id)
}
