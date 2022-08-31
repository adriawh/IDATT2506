package ntnu.adriawh.oving7.managers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

open class DatabaseManager(context: Context) :
		SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

	companion object {

		const val DATABASE_NAME = "MovieDatabase"
		const val DATABASE_VERSION = 1

		const val ID = "_id"

		const val TABLE_DIRECTOR = "DIRECTOR"
		const val DIRECTOR_NAME = "name"

		const val TABLE_MOVIE = "MOVIE"
		const val MOVIE_TITLE = "title"

		const val TABLE_AUTHOR_BOOK = "AUTHOR_BOOK"
		const val AUTHOR_ID = "author_id"
		const val BOOK_ID = "book_id"

		val JOIN_AUTHOR_BOOK = arrayOf(
				"$TABLE_AUTHOR.$ID=$TABLE_AUTHOR_BOOK.$AUTHOR_ID",
				"$TABLE_BOOK.$ID=$TABLE_AUTHOR_BOOK.$BOOK_ID"
		                              )
	}

	/**
	 *  Create the tables
	 */
	override fun onCreate(db: SQLiteDatabase) {
		db.execSQL(
				"""create table $TABLE_AUTHOR (
						$ID integer primary key autoincrement, 
						$AUTHOR_NAME text unique not null
						);"""
		          )
		db.execSQL(
				"""create table $TABLE_BOOK (
						$ID integer primary key autoincrement, 
						$BOOK_TITLE text unique not null
						);"""
		          )
		db.execSQL(
				"""create table $TABLE_AUTHOR_BOOK (
						$ID integer primary key autoincrement, 
						$BOOK_ID numeric, 
						$AUTHOR_ID numeric,
						FOREIGN KEY($AUTHOR_ID) REFERENCES $TABLE_AUTHOR($ID), 
						FOREIGN KEY($BOOK_ID) REFERENCES $TABLE_BOOK($ID)
						);"""
		          )
	}

	/**
	 * Drop and recreate all the tables
	 */
	override fun onUpgrade(db: SQLiteDatabase, arg1: Int, arg2: Int) {
		db.execSQL("DROP TABLE IF EXISTS $TABLE_AUTHOR_BOOK")
		db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOK")
		db.execSQL("DROP TABLE IF EXISTS $TABLE_AUTHOR")
		onCreate(db)
	}

	/**
	 *  Drop all information is the database
	 */
	fun clear() {
		writableDatabase.use { onUpgrade(it, 0, 0) }
	}


	/**
	 *  Inserts the author, the book, and then the connection between them.
	 */
	fun insert(author: String, book: String) {
		writableDatabase.use { database ->
			val authorId = insertValueIfNotExists(database, TABLE_AUTHOR, AUTHOR_NAME, author)
			val bookId = insertValueIfNotExists(database, TABLE_BOOK, BOOK_TITLE, book)
			linkAuthorAndBook(database, authorId, bookId)
		}
	}

	/**
	 * Pseudo-code for method
	 * ```
	 *  if (value exists){
	 *      return valueId
	 *  } else {
	 *      insert and return new Id
	 *  }
	 * ```
	 */
	private fun insertValueIfNotExists(
		database: SQLiteDatabase, table: String, field: String, value: String
	                                  ): Long {
		// Query for the value
		query(database, table, arrayOf(ID, field), "$field='$value'").use { cursor ->
			// insert if value doesn't exist
			return if (cursor.count != 0) {
				cursor.moveToFirst()
				cursor.getLong(0) //id of found value
			} else {
				insertValue(database, table, field, value)
			}
		}
	}



	/**
	 * Insert the value in given table and field, then return its ID
	 */
	private fun insertValue(
		database: SQLiteDatabase, table: String, field: String, value: String
	                       ): Long {
		val values = ContentValues()
		values.put(field, value.trim())
		return database.insert(table, null, values)
	}

	/**
	 *  Insert in the *TABLE_AUTHOR_BOOK*, essentially linking an author and a book
	 */
	private fun linkAuthorAndBook(database: SQLiteDatabase, authorId: Long, bookId: Long) {
		val values = ContentValues()
		values.put(AUTHOR_ID, authorId)
		values.put(BOOK_ID, bookId)
		database.insert(TABLE_AUTHOR_BOOK, null, values)
	}

	/**
	 * Perform a simple query
	 *
	 * Not the query() function has almost all parameters as *null*, you should check up on these.
	 * maybe you don't even need the performRawQuery() function?
	 */
	fun performQuery(table: String, columns: Array<String>, selection: String? = null):
			ArrayList<String> {
		assert(columns.isNotEmpty())
		readableDatabase.use { database ->
			query(database, table, columns, selection).use { cursor ->
				return readFromCursor(cursor, columns.size)
			}
		}
	}

	/**
	 * Run a raw query, the parameters are for easier debugging and reusable code
	 */
	fun performRawQuery(
		select: Array<String>, from: Array<String>, join: Array<String>, where: String? = null
	                   ): ArrayList<String> {

		val query = StringBuilder("SELECT ")
		for ((i, field) in select.withIndex()) {
			query.append(field)
			if (i != select.lastIndex) query.append(", ")
		}

		query.append(" FROM ")
		for ((i, table) in from.withIndex()) {
			query.append(table)
			if (i != from.lastIndex) query.append(", ")
		}

		query.append(" WHERE ")
		for ((i, link) in join.withIndex()) {
			query.append(link)
			if (i != join.lastIndex) query.append(" and ")
		}

		if (where != null) query.append(" and $where")

		readableDatabase.use { db ->
			db.rawQuery("$query;", null).use { cursor ->
				return readFromCursor(cursor, select.size)
			}
		}
	}

	/**
	 * Read the contents from the cursor and return it as an arraylist
	 */
	private fun readFromCursor(cursor: Cursor, numberOfColumns: Int): ArrayList<String> {
		val result = ArrayList<String>()
		cursor.moveToFirst()
		while (!cursor.isAfterLast) {
			val item = StringBuilder("")
			for (i in 0 until numberOfColumns) {
				item.append("${cursor.getString(i)} ")
			}
			result.add("$item")
			cursor.moveToNext()
		}
		return result
	}

	/**
	 * Use a query with default arguments
	 */
	private fun query(
		database: SQLiteDatabase, table: String, columns: Array<String>, selection: String?
	                 ): Cursor {
		return database.query(table, columns, selection, null, null, null, null, null)
	}
}
