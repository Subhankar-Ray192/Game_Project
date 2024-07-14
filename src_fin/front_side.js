// Get all cells
const cells = document.querySelectorAll('.cell');

// Variable to keep track of whether it's the AI's turn
let isAITurn = true; // AI always goes first

// Function to disable all cells
function disableAllCells() {
    cells.forEach(cell => {
        cell.removeEventListener('click', cellClickHandler);
    });
}

// Function to enable all cells
function enableAllCells() {
    cells.forEach(cell => {
        cell.addEventListener('click', cellClickHandler);
    });
}

// Function to clear the grid
function clearGrid() {
    cells.forEach(cell => {
        cell.textContent = ''; // Clear the cell content
        cell.style.backgroundImage = ''; // Clear the background image
    });
}

// Function to handle cell click
function cellClickHandler() {
    const cell = this; // 'this' refers to the clicked cell
    if (!cell.textContent && !isAITurn) {
        cell.style.backgroundImage = 'url("o_image.png")'; // Path to your "O" image
        // Add your game logic here
        disableAllCells(); // Disable all cells after a move
        isAITurn = true; // Set it to AI's turn
        // Call the function for AI move
        aiMove();
    }
}

// Function to handle AI move
function aiMove() {
    // Generate random row and column for AI move (example)
    const row = Math.floor(Math.random() * 3);
    const col = Math.floor(Math.random() * 3);
    // Place "X" at the generated row and column
    placeX(row, col);
    // Re-enable all cells for the player to make their move
    isAITurn = false; // Set it back to player's turn
    enableAllCells();
}

// Function to place "X" in a specific row and column
function placeX(row, col) {
    const cellId = `cell-${row * 3 + col}`;
    const cell = document.getElementById(cellId);

    if (cell && !cell.textContent) {
        cell.style.backgroundImage = 'url("x_image.png")'; // Path to your "X" image
        // Add your game logic here
    }
}

// Get the play button element
const playButton = document.getElementById('playButton');

// Add click event listener to the play button
playButton.addEventListener('click', () => {
    // Clear the grid
    clearGrid();
    // Reset the game by re-enabling all cells
    enableAllCells();
    // Call the function for AI move to start the game
    aiMove();
});

// Disable all cells initially
disableAllCells();
