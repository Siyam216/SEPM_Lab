// Authentication utility functions

/**
 * Get the JWT token from session storage
 */
function getAuthToken() {
    return sessionStorage.getItem('jwtToken');
}

/**
 * Get user information from session storage
 */
function getUserInfo() {
    return {
        token: sessionStorage.getItem('jwtToken'),
        role: sessionStorage.getItem('userRole'),
        email: sessionStorage.getItem('userEmail'),
        id: sessionStorage.getItem('userId'),
        name: sessionStorage.getItem('userName'),
        status: sessionStorage.getItem('userStatus')
    };
}

/**
 * Check if user is authenticated
 */
function isAuthenticated() {
    return !!getAuthToken();
}

/**
 * Make an authenticated API request
 * @param {string} url - The API endpoint URL
 * @param {Object} options - Fetch options (method, body, headers, etc.)
 * @returns {Promise<Response>}
 */
async function fetchWithAuth(url, options = {}) {
    const token = getAuthToken();

    // Add authorization header if token exists
    const headers = {
        ...options.headers,
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    // Add Content-Type for JSON requests
    if (options.body && typeof options.body === 'object') {
        headers['Content-Type'] = 'application/json';
        options.body = JSON.stringify(options.body);
    }

    const response = await fetch(url, {
        ...options,
        headers
    });

    // Handle unauthorized responses
    if (response.status === 401 || response.status === 403) {
        // Clear session and redirect to login
        sessionStorage.clear();
        alert('Your session has expired. Please login again.');
        window.location.href = '/login';
        throw new Error('Unauthorized');
    }

    return response;
}

/**
 * Logout user
 */
function logout() {
    sessionStorage.clear();
    window.location.href = '/login';
}

/**
 * Redirect to login if not authenticated
 */
function requireAuth() {
    if (!isAuthenticated()) {
        sessionStorage.clear();
        window.location.href = '/login';
        return false;
    }
    return true;
}

/**
 * Check if user has a specific role
 * @param {string} requiredRole - The role to check (e.g., 'TEACHER', 'STUDENT')
 */
function hasRole(requiredRole) {
    const userRole = sessionStorage.getItem('userRole');
    if (!userRole) return false;
    return userRole.toUpperCase() === requiredRole.toUpperCase();
}

/**
 * Redirect to login if user doesn't have required role
 */
function requireRole(requiredRole) {
    if (!requireAuth()) return false;

    if (!hasRole(requiredRole)) {
        alert(`Access denied. This page requires ${requiredRole} role.`);
        window.location.href = '/login';
        return false;
    }
    return true;
}
