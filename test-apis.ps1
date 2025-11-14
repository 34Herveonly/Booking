# Booking Service API Test Script
# This script tests all API endpoints with sample data

# Base URL
$baseUrl = "http://localhost:8076"
$userId = "test-user-$(Get-Date -Format 'HHmmss')"

Write-Host "=== Booking Service API Test Suite ===" -ForegroundColor Green
Write-Host "Base URL: $baseUrl" -ForegroundColor Yellow
Write-Host "Test User ID: $userId" -ForegroundColor Yellow
Write-Host ""

# Test 1: API Health Check
Write-Host "1. Testing API Health Check..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/bookings/test" -Method Get
    Write-Host "✓ API Test: $response" -ForegroundColor Green
} catch {
    Write-Host "✗ API Test Failed: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 2: Create Booking
Write-Host "2. Testing Create Booking..." -ForegroundColor Cyan
$bookingRequest = @{
    userId = $userId
    passengerName = "Test User PowerShell"
    passengerEmail = "testuser@example.com"
    passengerPhone = "+250781234567"
    departureProvince = "Kigali City"
    departureDistrict = "Gasabo"
    destinationProvince = "Eastern Province"
    destinationDistrict = "Rwamagana"
    travelDate = (Get-Date).AddDays(1).ToString("yyyy-MM-ddTHH:mm:ss")
    price = 2500.00
    numberOfPassengers = 2
    seatNumbers = "A1, A2"
    vehicleInfo = "Bus - PowerShell Test Express"
} | ConvertTo-Json

try {
    $createResponse = Invoke-RestMethod -Uri "$baseUrl/bookings/book" -Method Post -Body $bookingRequest -ContentType "application/json"
    $bookingId = $createResponse.bookingId
    Write-Host "✓ Booking Created: $bookingId" -ForegroundColor Green
    Write-Host "  - User: $($createResponse.userId)" -ForegroundColor Gray
    Write-Host "  - Passenger: $($createResponse.passengerName)" -ForegroundColor Gray
    Write-Host "  - Status: $($createResponse.status)" -ForegroundColor Gray
    Write-Host "  - Price: $($createResponse.price)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Create Booking Failed: $($_.Exception.Message)" -ForegroundColor Red
    exit
}
Write-Host ""

# Test 3: Get User Bookings
Write-Host "3. Testing Get User Bookings..." -ForegroundColor Cyan
try {
    $userBookings = Invoke-RestMethod -Uri "$baseUrl/bookings/user/bookings?userId=$userId" -Method Get
    Write-Host "✓ User Bookings Retrieved: $($userBookings.Count) booking(s)" -ForegroundColor Green
    foreach ($booking in $userBookings) {
        Write-Host "  - $($booking.bookingId): $($booking.passengerName) -> $($booking.destinationDistrict)" -ForegroundColor Gray
    }
} catch {
    Write-Host "✗ Get User Bookings Failed: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 4: Get Booking Details
Write-Host "4. Testing Get Booking Details..." -ForegroundColor Cyan
try {
    $bookingDetails = Invoke-RestMethod -Uri "$baseUrl/bookings/user/bookings/$bookingId?userId=$userId" -Method Get
    Write-Host "✓ Booking Details Retrieved for: $($bookingDetails.bookingId)" -ForegroundColor Green
    Write-Host "  - Travel Date: $($bookingDetails.travelDate)" -ForegroundColor Gray
    Write-Host "  - Route: $($bookingDetails.departureDistrict) -> $($bookingDetails.destinationDistrict)" -ForegroundColor Gray
    Write-Host "  - Passengers: $($bookingDetails.numberOfPassengers)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Get Booking Details Failed: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 5: Get Booking History
Write-Host "5. Testing Get Booking History..." -ForegroundColor Cyan
try {
    $bookingHistory = Invoke-RestMethod -Uri "$baseUrl/bookings/user/history?userId=$userId" -Method Get
    Write-Host "✓ Booking History Retrieved: $($bookingHistory.Count) booking(s)" -ForegroundColor Green
} catch {
    Write-Host "✗ Get Booking History Failed: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 6: Get User Stats
Write-Host "6. Testing Get User Stats..." -ForegroundColor Cyan
try {
    $userStats = Invoke-RestMethod -Uri "$baseUrl/bookings/user/stats?userId=$userId" -Method Get
    Write-Host "✓ User Stats Retrieved:" -ForegroundColor Green
    Write-Host "  - Total Bookings: $($userStats.totalBookings)" -ForegroundColor Gray
    Write-Host "  - Confirmed: $($userStats.confirmedBookings)" -ForegroundColor Gray
    Write-Host "  - Cancelled: $($userStats.cancelledBookings)" -ForegroundColor Gray
    Write-Host "  - Completed: $($userStats.completedBookings)" -ForegroundColor Gray
    Write-Host "  - Total Spent: `$$($userStats.totalSpent)" -ForegroundColor Gray
    Write-Host "  - Most Visited: $($userStats.mostVisitedDestination)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Get User Stats Failed: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 7: Download PDF Ticket
Write-Host "7. Testing Download PDF Ticket..." -ForegroundColor Cyan
try {
    $pdfResponse = Invoke-WebRequest -Uri "$baseUrl/bookings/$bookingId/download-pdf?userId=$userId" -Method Post
    if ($pdfResponse.Headers['Content-Type'] -contains 'application/pdf') {
        $pdfPath = "ticket-$bookingId.pdf"
        [System.IO.File]::WriteAllBytes($pdfPath, $pdfResponse.Content)
        Write-Host "✓ PDF Ticket Downloaded: $pdfPath" -ForegroundColor Green
        Write-Host "  - Size: $($pdfResponse.Content.Length) bytes" -ForegroundColor Gray
    } else {
        Write-Host "✗ PDF Download Failed: Invalid content type" -ForegroundColor Red
    }
} catch {
    Write-Host "✗ PDF Download Failed: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 8: Cancel Booking
Write-Host "8. Testing Cancel Booking..." -ForegroundColor Cyan
try {
    $cancelResponse = Invoke-RestMethod -Uri "$baseUrl/bookings/$bookingId/cancel?userId=$userId" -Method Put
    Write-Host "✓ Booking Cancelled: $($cancelResponse.bookingId)" -ForegroundColor Green
    Write-Host "  - New Status: $($cancelResponse.status)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Cancel Booking Failed: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 9: Verify Stats After Cancellation
Write-Host "9. Testing Stats After Cancellation..." -ForegroundColor Cyan
try {
    $finalStats = Invoke-RestMethod -Uri "$baseUrl/bookings/user/stats?userId=$userId" -Method Get
    Write-Host "✓ Final User Stats:" -ForegroundColor Green
    Write-Host "  - Total Bookings: $($finalStats.totalBookings)" -ForegroundColor Gray
    Write-Host "  - Confirmed: $($finalStats.confirmedBookings)" -ForegroundColor Gray
    Write-Host "  - Cancelled: $($finalStats.cancelledBookings)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Final Stats Failed: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 10: Error Cases
Write-Host "10. Testing Error Cases..." -ForegroundColor Cyan

# Test invalid booking ID
try {
    Invoke-RestMethod -Uri "$baseUrl/bookings/user/bookings/INVALID-ID?userId=$userId" -Method Get -ErrorAction Stop
    Write-Host "✗ Should have failed for invalid booking ID" -ForegroundColor Red
} catch {
    Write-Host "✓ Correctly handled invalid booking ID" -ForegroundColor Green
}

# Test cancelling already cancelled booking
try {
    Invoke-RestMethod -Uri "$baseUrl/bookings/$bookingId/cancel?userId=$userId" -Method Put -ErrorAction Stop
    Write-Host "✗ Should have failed for already cancelled booking" -ForegroundColor Red
} catch {
    Write-Host "✓ Correctly handled already cancelled booking" -ForegroundColor Green
}

Write-Host ""
Write-Host "=== Test Suite Completed ===" -ForegroundColor Green
Write-Host "All API endpoints have been tested!" -ForegroundColor Yellow