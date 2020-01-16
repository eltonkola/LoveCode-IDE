
data class Doks (

	val callbacks : List<Callbacks>,
	val functions : List<Functions>,
	val modules : List<Modules>,
	val types : List<Types>,
	val version : Double
)

data class Modules (

	val description : String,
	val enums : List<Enums>,
	val functions : List<Functions>,
	val name : String,
	val types : List<Types>
)

data class Arguments (

	val description : String,
	val name : String,
	val table : List<Table>,
	val type : String
)

data class Callbacks (

	val description : String,
	val name : String,
	val variants : List<Variants>
)


data class Constants (

	val description : String,
	val name : String
)


data class Enums (

	val constants : List<Constants>,
	val description : String,
	val name : String
)

data class Functions (

	val description : String,
	val name : String,
	val variants : List<Variants>
)


data class Returns (

	val description : String,
	val name : String,
	val type : String
)


data class Table (

	val default : String,
	val description : String,
	val name : String,
	val type : String
)

data class Types (

	val description : String,
	val functions : List<String>,
	val name : String,
	val supertypes : List<String>
)

data class Variants (

	val returns : List<Returns>
)
