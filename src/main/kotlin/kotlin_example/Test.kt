package kotlin_example

data class Test(var name: String, var age: Int)

fun getName(name : String?): String? {
    return name // !! -> 자바의 npe를 그대로 터뜨리는 연산자, 보통 안전하지 않는 방법이라, 권장아님
    // return name ?: "no name"
    // return name?.uppercase()
}

fun main() {
    var person1 = Test("tester1", 25)
    var person2 = Test("tester1", 25)

    println("동등성 체크 " +  (person1 == person2))
    println("동일성 체크 " + (person1 === person2))

    println(person1)
    var person3 = person1.copy("tester3");
    println(person3)
    println("person1 name : " + getName(person1.name))

    val nullableStr: String? = getName("tony");
    print(nullableStr)


}


