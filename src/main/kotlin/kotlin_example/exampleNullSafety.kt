//package kotlin_example
//
//class ExampleNullSafety {
//
//    fun basicNullSafety() {
//        var nullableString: String? = "hello"
//        var nonnullableString: String = "world"
//
//        println(nullableString)
//        println(nonnullableString)
//
//        nullableString = null
//        //nonnullableString = null
//        println(nullableString)
//        println(nonnullableString)
//    }
//
//    fun safeCallOperator() {
//        val name1: String? = "kotlin"
//        val name2: String? = null
//        val name3: String = "tony"
//
//        println(name1?.length) // ?는 null이 들어올 수 있으니 안전하게 ?.로 호출하는구나
//        println(name2?.length)
//        println(name3.length)
//
//        val text: String? = "  Hello World   "
//        val result = text?.trim()?.uppercase()?.substring(0,5)
//        println(result)
//
//        val nullText: String? = null
//        val nullResult = nullText?.trim()?.uppercase()?.substring(0,5)
//        println(nullResult)
//
//    }
//
//    fun elvisOperator() {
//        val name: String? = null
//        val defaultName = "Unknown"
//
//        val actualName = name ?: defaultName
//        println(actualName)
//
//        fun getLength(str: String?): Int {
//            return str?.length ?: 0  // str?.length ?. 먼저 안전하게 호출하고, null이라면 ?: 엘비스 문법을 통해 디폴트값 할당.
//        }
//
//        println("Length of Hello : ${getLength("Hello")}")
//        println("Length of null: ${getLength(null)}")
//
//        fun processUser(user: User?): String {
//            val validUser = user ?: return "not found user"
//            return "user : ${validUser.name}"
//        }
//        //println(processUser(User("Alice")))
//        println(processUser(null))
//
//    }
//
//    fun nullAssertion() {
//        val nullableString: String? = "Hello"
//        val definitelyNotNull: String = nullableString!!
//        println("definitelyNotNull = $definitelyNotNull")
//
//        try{
//            val nullString: String? = null
//            val boom = nullString!!
//        } catch (e: NullPointerException) {
//            println("KotlinNullPointerException: ${e.message}")
//        }
//    }
//
//    fun safeCast(){
//        val obj1: Any = "Hello"
//        val obj2: Any = 123
//
//        val str: String? = obj1 as? String
//        println("safeCast : $str")
//
//        val str2: String? = obj2 as? String
//        println("fail safeCast : $str2")
//
//        fun processObject(obj: Any){
//            when (val result = obj as? String) {
//                null -> println("not String")
//                else -> println("is String")
//            }
//        }
//
//        processObject("Sring")
//        processObject(123)
//    }
//
//    fun letWithNull() {
//        val name: String? = "kotlin"
//        val nullName: String? = null
//
//        // let키워드 null이 아닐 때만 실행한다.
//        name?.let { validName ->
//            println(validName)
//            println(validName.length)
//        }
//
//        nullName?.let { validName ->
//            println("no enter")
//            println(validName)
//            println(validName.length)
//        }
//
//        val nameLength = name?.let{ it.length} ?:0 // let 블록 안에서 호출 대상 객체(여기서는 name이나 nullName)의 값을 가리키는 암시적 파라미터 이름
//        println(nameLength)
//    }
//
//    fun runWithNull() { // 해당 객체를 기반으로 뭔가 계산해서 결과를 리턴"할 때 적합
//        val user: User? = User("Tony")
//        val nullUser: User? = null
//
//        val userInfo = user?.run {
//            "이름: $name, 이름 길이: ${(name?.length ?: 0) > 2}"
//        } ?: "not fount user"
//
//        println(userInfo)
//    }
//
//    fun applyAlsoWithNull() {
//        val user: User? = User("Tony")
//
//        user?.also { validUser ->
//            println("1 ${validUser.name}") }
//                ?.also { validUser ->
//                println("2 ${validUser.name}")
//            }
//
//        val modifiedUser = user?.let { existingUser ->
//            User("tony").apply { // builder 패턴과 유사
//               name = "lee doyeon"
//            }
//        }
//        println(modifiedUser)
//
//        }
//    }
//
//
//fun main() {
//    var test = ExampleNullSafety()
//
//    //test.basicNullSafety()
//    //test.safeCallOperator()
//    //test.safeCallOperator()
//    //test.elvisOperator()
//    //test.nullAssertion()
//    //test.safeCast()
//    //test.letWithNull()
//    //test.runWithNull()
//    test.applyAlsoWithNull()
//}