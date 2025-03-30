document.addEventListener('DOMContentLoaded', () => {
    const newEntryBtn = document.getElementById('newEntryBtn');
    const entryForm = document.getElementById('entryForm');
    const journalForm = document.getElementById('journalForm');
    const cancelBtn = document.getElementById('cancelBtn');
    const entriesList = document.getElementById('entriesList');
    const searchInput = document.getElementById('searchInput');
    const toast = document.getElementById('toast');
    const toastMessage = document.getElementById('toastMessage');
    let editingId = null;
    let entries = [];

    // Show/hide the entry form
    newEntryBtn.addEventListener('click', () => {
        editingId = null;
        journalForm.reset();
        entryForm.classList.remove('hidden');
        showToast('Creating new entry');
    });

    cancelBtn.addEventListener('click', () => {
        entryForm.classList.add('hidden');
        journalForm.reset();
        editingId = null;
    });

    // Handle form submission
    journalForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const title = document.getElementById('title').value;
        const content = document.getElementById('content').value;
        const url = editingId ? `/abc/id/${editingId}` : '/abc';
        const method = editingId ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ title, content }),
            });

            if (response.ok) {
                journalForm.reset();
                entryForm.classList.add('hidden');
                editingId = null;
                showToast(editingId ? 'Entry updated successfully' : 'Entry created successfully');
                loadEntries();
            } else {
                throw new Error('Failed to save entry');
            }
        } catch (error) {
            console.error('Error saving entry:', error);
            showToast('Error saving entry', 'error');
        }
    });

    // Search functionality
    searchInput.addEventListener('input', (e) => {
        const searchTerm = e.target.value.toLowerCase();
        const filteredEntries = entries.filter(entry => 
            entry.title.toLowerCase().includes(searchTerm) || 
            entry.content.toLowerCase().includes(searchTerm)
        );
        displayEntries(filteredEntries);
    });

    // Load and display entries
    async function loadEntries() {
        try {
            const response = await fetch('/abc');
            entries = await response.json();
            displayEntries(entries);
        } catch (error) {
            console.error('Error loading entries:', error);
            showToast('Error loading entries', 'error');
        }
    }

    function displayEntries(entriesToShow) {
        if (entriesToShow.length === 0) {
            entriesList.innerHTML = '<p class="no-entries">No entries found. Create your first journal entry!</p>';
            return;
        }

        entriesList.innerHTML = entriesToShow.map(entry => `
            <div class="entry-card">
                <h3><i class="fas fa-bookmark"></i> ${entry.title}</h3>
                <p>${entry.content}</p>
                <div class="entry-date">
                    <i class="fas fa-calendar"></i>
                    ${new Date(entry.createdAt).toLocaleDateString()}
                </div>
                <div class="entry-actions">
                    <button class="btn-primary edit-btn" onclick="editEntry('${entry.id}', '${entry.title.replace(/'/g, "\\'")}', '${entry.content.replace(/'/g, "\\'")}')">
                        <i class="fas fa-edit"></i> Edit
                    </button>
                    <button class="btn-secondary delete-btn" onclick="deleteEntry('${entry.id}')">
                        <i class="fas fa-trash"></i> Delete
                    </button>
                </div>
            </div>
        `).join('');
    }

    // Edit entry function
    window.editEntry = (id, title, content) => {
        editingId = id;
        document.getElementById('title').value = title;
        document.getElementById('content').value = content;
        entryForm.classList.remove('hidden');
        showToast('Editing entry');
    };

    // Delete entry function
    window.deleteEntry = async (id) => {
        if (!id) {
            console.error('No entry ID provided');
            return;
        }

        if (confirm('Are you sure you want to delete this entry?')) {
            try {
                const response = await fetch(`/abc/id/${id}`, {
                    method: 'DELETE'
                });

                if (response.ok) {
                    showToast('Entry deleted successfully');
                    loadEntries();
                } else {
                    throw new Error('Failed to delete entry');
                }
            } catch (error) {
                console.error('Error deleting entry:', error);
                showToast('Error deleting entry', 'error');
            }
        }
    };

    // Toast notification function
    function showToast(message, type = 'success') {
        toast.className = `toast ${type}`;
        toastMessage.textContent = message;
        toast.classList.remove('hidden');
        
        setTimeout(() => {
            toast.classList.add('hidden');
        }, 3000);
    }

    // Initial load of entries
    loadEntries();
}); 