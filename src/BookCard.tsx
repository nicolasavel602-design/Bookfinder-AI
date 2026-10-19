import './BookCard.css'

type BookCardProps = {
    title: string
    author: string
    thumbnail: string
}

//displays the books on the webpage in a bookcard format
function BookCard({title, author, thumbnail}: BookCardProps){
    return(
        <article className="book-card">
            {thumbnail ? (
                <img
                    className="book-cover"
                    src={thumbnail}
                    alt={`Cover of ${title}`}
                />
            ) : (
                <div className="book-cover-placeholder">
                    No cover available
                </div>
            )}


            <div className="book-info">
                <h2> {title} </h2>
                <p> By {author} </p>
            </div>
        </article>
    )
}

export default BookCard