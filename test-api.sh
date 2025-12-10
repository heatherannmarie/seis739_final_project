#!/bin/bash

# =============================================================================
# Safe Spend - End-to-End API Test Script
# =============================================================================
# This script tests all the API endpoints for the allowance tracker application.
# Prerequisites: Backend running on localhost:8080, curl installed
# Usage: chmod +x test-api.sh && ./test-api.sh
# =============================================================================

BASE_URL="http://localhost:8080/api"
PASS=0
FAIL=0

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Helper function to print section headers
section() {
    echo ""
    echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
    echo -e "${BLUE}  $1${NC}"
    echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
}

# Helper function to print test results
test_result() {
    local name="$1"
    local expected="$2"
    local actual="$3"
    
    if echo "$actual" | grep -q "$expected"; then
        echo -e "${GREEN}✓ PASS${NC}: $name"
        ((PASS++))
        return 0
    else
        echo -e "${RED}✗ FAIL${NC}: $name"
        echo -e "  Expected to contain: ${YELLOW}$expected${NC}"
        echo -e "  Got: ${YELLOW}$actual${NC}"
        ((FAIL++))
        return 1
    fi
}

# Helper function for API calls
api_call() {
    local method="$1"
    local endpoint="$2"
    local data="$3"
    
    if [ -z "$data" ]; then
        curl -s -X "$method" "$BASE_URL$endpoint" -H "Content-Type: application/json"
    else
        curl -s -X "$method" "$BASE_URL$endpoint" -H "Content-Type: application/json" -d "$data"
    fi
}

# =============================================================================
# Check if backend is running
# =============================================================================
section "Checking Backend Connection"

HEALTH_CHECK=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/parents/test" 2>/dev/null || echo "000")

if [ "$HEALTH_CHECK" = "000" ]; then
    echo -e "${RED}ERROR: Cannot connect to backend at $BASE_URL${NC}"
    echo "Please make sure the Spring Boot application is running on port 8080"
    echo ""
    echo "To start the backend:"
    echo "  cd backend"
    echo "  ./mvnw spring-boot:run"
    exit 1
fi

echo -e "${GREEN}Backend is reachable${NC}"

# =============================================================================
# PARENT TESTS
# =============================================================================
section "Testing Parent Endpoints"

# Create a parent
echo -e "\n${YELLOW}Creating parent...${NC}"
PARENT_RESPONSE=$(api_call POST "/parents" '{"name": "John Parent"}')
echo "Response: $PARENT_RESPONSE"
test_result "Create parent" "parentId" "$PARENT_RESPONSE"

# Extract parent ID
PARENT_ID=$(echo "$PARENT_RESPONSE" | grep -o '"parentId":"[^"]*"' | cut -d'"' -f4)
echo -e "Parent ID: ${YELLOW}$PARENT_ID${NC}"

if [ -z "$PARENT_ID" ]; then
    echo -e "${RED}Failed to extract parent ID. Exiting.${NC}"
    exit 1
fi

# Get parent
echo -e "\n${YELLOW}Getting parent...${NC}"
GET_PARENT=$(api_call GET "/parents/$PARENT_ID")
echo "Response: $GET_PARENT"
test_result "Get parent" "John Parent" "$GET_PARENT"

# =============================================================================
# CHILD TESTS
# =============================================================================
section "Testing Child Endpoints"

# Create a child
echo -e "\n${YELLOW}Creating child...${NC}"
CHILD_RESPONSE=$(api_call POST "/parents/$PARENT_ID/children" '{"name": "Emma Child", "username": "emma123"}')
echo "Response: $CHILD_RESPONSE"
test_result "Create child" "childId" "$CHILD_RESPONSE"

# Extract child ID
CHILD_ID=$(echo "$CHILD_RESPONSE" | grep -o '"childId":"[^"]*"' | cut -d'"' -f4)
echo -e "Child ID: ${YELLOW}$CHILD_ID${NC}"

# Get children
echo -e "\n${YELLOW}Getting children list...${NC}"
GET_CHILDREN=$(api_call GET "/parents/$PARENT_ID/children")
echo "Response: $GET_CHILDREN"
test_result "Get children" "Emma Child" "$GET_CHILDREN"

# Update child
echo -e "\n${YELLOW}Updating child...${NC}"
UPDATE_CHILD=$(api_call PUT "/parents/$PARENT_ID/children/$CHILD_ID" '{"name": "Emma Updated", "username": "emma_new"}')
echo "Response: $UPDATE_CHILD"
test_result "Update child" "Emma Updated" "$UPDATE_CHILD"

# Get child directly
echo -e "\n${YELLOW}Getting child directly...${NC}"
GET_CHILD=$(api_call GET "/children/$CHILD_ID")
echo "Response: $GET_CHILD"
test_result "Get child" "emma_new" "$GET_CHILD"

# Get child balance
echo -e "\n${YELLOW}Getting child balance...${NC}"
GET_BALANCE=$(api_call GET "/children/$CHILD_ID/balance")
echo "Response: $GET_BALANCE"
test_result "Get balance" "balance" "$GET_BALANCE"

# =============================================================================
# CHORE TESTS
# =============================================================================
section "Testing Chore Endpoints"

# Create a chore
echo -e "\n${YELLOW}Creating chore...${NC}"
CHORE_RESPONSE=$(api_call POST "/parents/$PARENT_ID/chores" '{
    "choreName": "Clean Room",
    "choreDescription": "Make bed, vacuum floor, organize desk",
    "chorePrice": 5.00
}')
echo "Response: $CHORE_RESPONSE"
test_result "Create chore" "choreId" "$CHORE_RESPONSE"

# Extract chore ID
CHORE_ID=$(echo "$CHORE_RESPONSE" | grep -o '"choreId":"[^"]*"' | cut -d'"' -f4)
echo -e "Chore ID: ${YELLOW}$CHORE_ID${NC}"

# Create another chore for testing
echo -e "\n${YELLOW}Creating second chore...${NC}"
CHORE2_RESPONSE=$(api_call POST "/parents/$PARENT_ID/chores" '{
    "choreName": "Do Dishes",
    "choreDescription": "Wash and dry all dishes",
    "chorePrice": 3.00
}')
CHORE2_ID=$(echo "$CHORE2_RESPONSE" | grep -o '"choreId":"[^"]*"' | cut -d'"' -f4)
test_result "Create second chore" "choreId" "$CHORE2_RESPONSE"

# Get chores
echo -e "\n${YELLOW}Getting chores list...${NC}"
GET_CHORES=$(api_call GET "/parents/$PARENT_ID/chores")
echo "Response: $GET_CHORES"
test_result "Get chores" "Clean Room" "$GET_CHORES"

# Update chore
echo -e "\n${YELLOW}Updating chore...${NC}"
UPDATE_CHORE=$(api_call PUT "/parents/$PARENT_ID/chores/$CHORE_ID" '{
    "choreName": "Clean Entire Room",
    "chorePrice": 7.50
}')
echo "Response: $UPDATE_CHORE"
test_result "Update chore" "7.5" "$UPDATE_CHORE"

# Get available chores (child endpoint)
echo -e "\n${YELLOW}Getting available chores for child...${NC}"
AVAILABLE_CHORES=$(api_call GET "/children/$CHILD_ID/available-chores")
echo "Response: $AVAILABLE_CHORES"
test_result "Get available chores" "Clean Entire Room" "$AVAILABLE_CHORES"

# =============================================================================
# CHORE COMPLETION FLOW
# =============================================================================
section "Testing Chore Completion Flow"

# Child requests completion
echo -e "\n${YELLOW}Child requesting chore completion...${NC}"
REQUEST_COMPLETION=$(api_call POST "/children/$CHILD_ID/chores/$CHORE_ID/request-completion")
echo "Response: $REQUEST_COMPLETION"
test_result "Request completion" "PENDING" "$REQUEST_COMPLETION"

# Get pending chores (parent)
echo -e "\n${YELLOW}Getting pending chores...${NC}"
PENDING_CHORES=$(api_call GET "/parents/$PARENT_ID/chores/pending")
echo "Response: $PENDING_CHORES"
test_result "Get pending chores" "$CHORE_ID" "$PENDING_CHORES"

# Parent approves chore
echo -e "\n${YELLOW}Parent approving chore...${NC}"
APPROVE_CHORE=$(api_call POST "/parents/$PARENT_ID/chores/$CHORE_ID/approve")
echo "Response: $APPROVE_CHORE"
test_result "Approve chore" "ALLOWANCE" "$APPROVE_CHORE"

# Check child balance increased
echo -e "\n${YELLOW}Checking child balance after approval...${NC}"
NEW_BALANCE=$(api_call GET "/children/$CHILD_ID/balance")
echo "Response: $NEW_BALANCE"
test_result "Balance increased" "7.5" "$NEW_BALANCE"

# Test deny flow with second chore
echo -e "\n${YELLOW}Child requesting second chore completion...${NC}"
REQUEST_COMPLETION2=$(api_call POST "/children/$CHILD_ID/chores/$CHORE2_ID/request-completion")
test_result "Request second completion" "PENDING" "$REQUEST_COMPLETION2"

echo -e "\n${YELLOW}Parent denying chore...${NC}"
DENY_CHORE=$(api_call POST "/parents/$PARENT_ID/chores/$CHORE2_ID/deny")
echo "Response: $DENY_CHORE"
test_result "Deny chore" "AVAILABLE" "$DENY_CHORE"

# =============================================================================
# STORE ITEM TESTS
# =============================================================================
section "Testing Store Item Endpoints"

# Create store item
echo -e "\n${YELLOW}Creating store item...${NC}"
ITEM_RESPONSE=$(api_call POST "/parents/$PARENT_ID/store-items" '{
    "itemName": "Extra Screen Time (30 min)",
    "itemPrice": 2.50,
    "availableInventory": 10
}')
echo "Response: $ITEM_RESPONSE"
test_result "Create store item" "itemID" "$ITEM_RESPONSE"

# Extract item ID
ITEM_ID=$(echo "$ITEM_RESPONSE" | grep -o '"itemID":"[^"]*"' | cut -d'"' -f4)
echo -e "Item ID: ${YELLOW}$ITEM_ID${NC}"

# Create another item
echo -e "\n${YELLOW}Creating second store item...${NC}"
ITEM2_RESPONSE=$(api_call POST "/parents/$PARENT_ID/store-items" '{
    "itemName": "Movie Night Pick",
    "itemPrice": 5.00,
    "availableInventory": 3
}')
ITEM2_ID=$(echo "$ITEM2_RESPONSE" | grep -o '"itemID":"[^"]*"' | cut -d'"' -f4)
test_result "Create second item" "itemID" "$ITEM2_RESPONSE"

# Get store items (parent)
echo -e "\n${YELLOW}Getting store items (parent)...${NC}"
GET_ITEMS_PARENT=$(api_call GET "/parents/$PARENT_ID/store-items")
echo "Response: $GET_ITEMS_PARENT"
test_result "Get store items (parent)" "Extra Screen Time" "$GET_ITEMS_PARENT"

# Get store items (child)
echo -e "\n${YELLOW}Getting store items (child)...${NC}"
GET_ITEMS_CHILD=$(api_call GET "/children/$CHILD_ID/store-items")
echo "Response: $GET_ITEMS_CHILD"
test_result "Get store items (child)" "Movie Night Pick" "$GET_ITEMS_CHILD"

# Update store item
echo -e "\n${YELLOW}Updating store item...${NC}"
UPDATE_ITEM=$(api_call PUT "/parents/$PARENT_ID/store-items/$ITEM_ID" '{
    "itemName": "Extra Screen Time (1 hour)",
    "itemPrice": 4.00
}')
echo "Response: $UPDATE_ITEM"
test_result "Update store item" "1 hour" "$UPDATE_ITEM"

# =============================================================================
# PURCHASE TESTS
# =============================================================================
section "Testing Purchase Flow"

# Child purchases item
echo -e "\n${YELLOW}Child purchasing item...${NC}"
PURCHASE=$(api_call POST "/children/$CHILD_ID/purchase/$ITEM_ID")
echo "Response: $PURCHASE"
test_result "Purchase item" "PURCHASE" "$PURCHASE"

# Check balance decreased
echo -e "\n${YELLOW}Checking balance after purchase...${NC}"
BALANCE_AFTER=$(api_call GET "/children/$CHILD_ID/balance")
echo "Response: $BALANCE_AFTER"
test_result "Balance decreased" "3.5" "$BALANCE_AFTER"

# Check inventory decreased
echo -e "\n${YELLOW}Checking inventory after purchase...${NC}"
INVENTORY_CHECK=$(api_call GET "/parents/$PARENT_ID/store-items")
echo "Response: $INVENTORY_CHECK"
test_result "Inventory decreased" '"availableInventory":9' "$INVENTORY_CHECK"

# =============================================================================
# TRANSACTION TESTS
# =============================================================================
section "Testing Transaction Endpoints"

# Get child transactions
echo -e "\n${YELLOW}Getting child transactions...${NC}"
CHILD_TRANSACTIONS=$(api_call GET "/children/$CHILD_ID/transactions")
echo "Response: $CHILD_TRANSACTIONS"
test_result "Child has allowance transaction" "ALLOWANCE" "$CHILD_TRANSACTIONS"
test_result "Child has purchase transaction" "PURCHASE" "$CHILD_TRANSACTIONS"

# Get parent transactions
echo -e "\n${YELLOW}Getting parent transactions...${NC}"
PARENT_TRANSACTIONS=$(api_call GET "/parents/$PARENT_ID/transactions")
echo "Response: $PARENT_TRANSACTIONS"
test_result "Parent sees transactions" "Clean Entire Room" "$PARENT_TRANSACTIONS"

# =============================================================================
# DELETE TESTS
# =============================================================================
section "Testing Delete Endpoints"

# Delete chore
echo -e "\n${YELLOW}Deleting chore...${NC}"
DELETE_CHORE=$(api_call DELETE "/parents/$PARENT_ID/chores/$CHORE2_ID")
echo "Response: $DELETE_CHORE"
test_result "Delete chore" "deleted successfully" "$DELETE_CHORE"

# Verify deletion
echo -e "\n${YELLOW}Verifying chore deletion...${NC}"
CHORES_AFTER_DELETE=$(api_call GET "/parents/$PARENT_ID/chores")
if echo "$CHORES_AFTER_DELETE" | grep -q "$CHORE2_ID"; then
    echo -e "${RED}✗ FAIL${NC}: Chore still exists after deletion"
    ((FAIL++))
else
    echo -e "${GREEN}✓ PASS${NC}: Chore successfully deleted"
    ((PASS++))
fi

# Delete store item
echo -e "\n${YELLOW}Deleting store item...${NC}"
DELETE_ITEM=$(api_call DELETE "/parents/$PARENT_ID/store-items/$ITEM2_ID")
echo "Response: $DELETE_ITEM"
test_result "Delete store item" "deleted successfully" "$DELETE_ITEM"

# =============================================================================
# SUMMARY
# =============================================================================
section "Test Summary"

TOTAL=$((PASS + FAIL))
echo ""
echo -e "Total Tests: ${YELLOW}$TOTAL${NC}"
echo -e "Passed:      ${GREEN}$PASS${NC}"
echo -e "Failed:      ${RED}$FAIL${NC}"
echo ""

if [ $FAIL -eq 0 ]; then
    echo -e "${GREEN}════════════════════════════════════════════${NC}"
    echo -e "${GREEN}  ALL TESTS PASSED! 🎉${NC}"
    echo -e "${GREEN}════════════════════════════════════════════${NC}"
    exit 0
else
    echo -e "${RED}════════════════════════════════════════════${NC}"
    echo -e "${RED}  SOME TESTS FAILED${NC}"
    echo -e "${RED}════════════════════════════════════════════${NC}"
    exit 1
fi
