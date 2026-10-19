import BookCard from './BookCard'
import { useState } from 'react'
import './App.css'

// Defines the structure of book data received from the Java backend.
type Book = {
  id: string
  title: string
  author: string
  description: string
  thumbnail: string
}

function App() {
  // React objects.
  const [query, setQuery] = useState('')
  const [books, setBooks] = useState<Book[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [hasSearched, setHasSearched] = useState(false)

  // Performs a standard book search without using AI.
  async function handleSearch() {
    if (!query.trim() || loading) return

    setLoading(true)
    setError('')
    setHasSearched(true)
    setBooks([])

    try {
      // Sends a GET request to the backend and recieves a response.
      const response = await fetch(
        `http://localhost:8080/api/books?query=${encodeURIComponent(query)}`
      )

      // Throws an error incase of a failed response, goes straight to catch.
      if (!response.ok) {
        throw new Error(`Request failed: ${response.status}`)
      }

      // The backend already returns books in our expected structure.
      const data: Book[] = await response.json()
      setBooks(data)

      // Logic for when there is an error.
    } catch (err) {
      setError('Something went wrong while searching for books.')
      console.error(err)

      // This always runs and will re-enable the search button.
    } finally {
      setLoading(false)
    }
  }

  // Requests AI-generated search keywords and retrives matching books.
  async function handleRecommendations() {
    if (!query.trim() || loading) return

    setLoading(true)
    setError('')
    setHasSearched(true)
    setBooks([])

    try {
      // This endpoint uses Groq to interpret the reader's preferences
      // before searching Open Library for relevant books.
      const response = await fetch(
         `http://localhost:8080/api/recommendations?query=${encodeURIComponent(query)}`
      )

      if (!response.ok){
        throw new Error(`Request failed: ${response.status}`)
      }

      const data: Book[] = await response.json()
      setBooks(data)


    } catch(err){
      setError('Something went wrong while searching for books.')
      console.error(err)

    }finally{
      setLoading(false)
    }
    
  }

  return (
    <main>
      <h1>BookFinder AI</h1>
      <p>Find your next favourite book</p>

      {/* This creates the search bar and keeps the search text synchronised with React state. */}
      <input
        className="search-input"
        type="text"
        value={query}
        onChange={(event) => setQuery(event.target.value)}
        onKeyDown={(event) => {
          if (event.key === 'Enter') handleSearch()
        }}
        placeholder="Describe the book you're looking for..."
      />

      {/* This creates the button. */}
      {/* If loading is true, the button will be disabled to prevent simultaneous requests. */}
      <div className="search-buttons">
        <button
          onClick={handleSearch}
          disabled={loading || !query.trim()}
        >
         Search books
        </button>

        <button
          onClick={handleRecommendations}
          disabled={loading || !query.trim()}
        >
          Recommend books
        </button>
      </div>

      {/*/ If there is an error, display it on the webpage. */}
      {error && <p role="alert">{error}</p>}

      {/* This message is displayed only if a search has finished successfully but returned no books*/}
      {!loading && !error && hasSearched && books.length === 0 && (
        <p>No books found. Try a different search.</p>
      )}

      {/* This runs only when there are books in the list */}
      {books.length > 0 && (
        <section>
          <h2>Search results</h2>

          {/* Loops through every book in the returned list and creates a Bookcard component for each book*/}
          {books.map((book) => (
            <BookCard
              key={book.id}
              title={book.title}
              author={book.author}
              thumbnail={book.thumbnail}
            />
          ))}
        </section>
      )}
    </main>
  )
}

export default App
