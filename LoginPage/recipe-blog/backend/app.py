from flask import Flask, request, jsonify
from flask_cors import CORS
import mysql.connector

app = Flask(__name__)
CORS(app)  # Allow CORS for all routes

# MySQL database connection
db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="your-password",
    database="receipeblog"
)

cursor = db.cursor()

# User Signup
@app.route('/signup', methods=['POST'])
def signup():
    data = request.json
    username = data.get('username')
    password = data.get('password')
    
    # Save user to MySQL
    cursor.execute("INSERT INTO users (username, password) VALUES (%s, %s)", (username, password))
    db.commit()
    
    return jsonify({"message": "User created successfully!"}), 201

# User Login
@app.route('/login', methods=['POST'])
def login():
    data = request.json
    username = data.get('username')
    password = data.get('password')
    
    # Check user credentials
    cursor.execute("SELECT * FROM users WHERE username = %s AND password = %s", (username, password))
    user = cursor.fetchone()
    
    if user:
        return jsonify({"message": "Login successful!", "user_id": user[0]}), 200
    else:
        return jsonify({"message": "Invalid credentials!"}), 401

# Add New Recipe
@app.route('/recipes', methods=['POST'])
def add_recipe():
    data = request.json
    title = data.get('title')
    description = data.get('description')
    category_id = data.get('category_id')
    user_id = data.get('user_id')

    cursor.execute("INSERT INTO recipes (title, description, category_id, user_id) VALUES (%s, %s, %s, %s)", 
                   (title, description, category_id, user_id))
    db.commit()
    recipe_id = cursor.lastrowid

    return jsonify({"message": "Recipe created successfully!", "recipe_id": recipe_id}), 201

# Get Recipes
@app.route('/recipes', methods=['GET'])
def get_recipes():
    cursor.execute("SELECT * FROM recipes")
    recipes = cursor.fetchall()
    
    recipe_list = []
    for recipe in recipes:
        recipe_list.append({
            "recipe_id": recipe[0],
            "title": recipe[1],
            "description": recipe[2],
            "category_id": recipe[3],
            "user_id": recipe[4]
        })

    return jsonify(recipe_list), 200

# Add Ingredient to Recipe
@app.route('/recipes/<int:recipe_id>/ingredients', methods=['POST'])
def add_ingredient(recipe_id):
    data = request.json
    ingredient_name = data.get('name')
    quantity = data.get('quantity')

    cursor.execute("INSERT INTO ingredients (recipe_id, name, quantity) VALUES (%s, %s, %s)", 
                   (recipe_id, ingredient_name, quantity))
    db.commit()

    return jsonify({"message": "Ingredient added successfully!"}), 201

# Add Comment to Recipe
@app.route('/recipes/<int:recipe_id>/comments', methods=['POST'])
def add_comment(recipe_id):
    data = request.json
    user_id = data.get('user_id')
    comment_text = data.get('text')

    cursor.execute("INSERT INTO comments (recipe_id, user_id, text) VALUES (%s, %s, %s)", 
                   (recipe_id, user_id, comment_text))
    db.commit()

    return jsonify({"message": "Comment added successfully!"}), 201

# Get Comments for Recipe
@app.route('/recipes/<int:recipe_id>/comments', methods=['GET'])
def get_comments(recipe_id):
    cursor.execute("SELECT * FROM comments WHERE recipe_id = %s", (recipe_id,))
    comments = cursor.fetchall()
    
    comment_list = []
    for comment in comments:
        comment_list.append({
            "comment_id": comment[0],
            "recipe_id": comment[1],
            "user_id": comment[2],
            "text": comment[3]
        })

    return jsonify(comment_list), 200

# Get Categories
@app.route('/categories', methods=['GET'])
def get_categories():
    cursor.execute("SELECT * FROM categories")
    categories = cursor.fetchall()
    
    category_list = []
    for category in categories:
        category_list.append({
            "category_id": category[0],
            "name": category[1]
        })

    return jsonify(category_list), 200

if __name__ == '__main__':
    app.run(debug=True)
