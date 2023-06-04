#!/usr/bin/env bash

# Directory name
dir="hbr-backend"

# Check if directory exists
if [ -d "$dir" ]; then
  # If directory exists, warn about it
  echo "Warning: Directory $dir already exists."
  echo "Will create a new directory with timestamp appended"
  # Create a timestamped directory instead
  timestamp=$(date +%Y%m%d%H%M%S%3N)
  dir="${dir}_${timestamp}"
fi

# Create a new directory and enter it
mkdir "$dir"
cd "$dir"

# Create assets directories
mkdir views
mkdir -p public/css
# Copy favicon
cp ../favicon.ico public

# Initiate a new Node.js project
npm init -y

# Install the necessary packages
npm install autoprefixer body-parser cors dotenv ejs express express-jwt express-session jimp jsonwebtoken jwks-rsa keycloak-backend keycloak-connect openid-client passport passport-jwt postcss-cli serve-favicon tailwindcss ts-node typescript
npm i --save-dev @types/cors @types/express @types/express-session @types/passport @types/passport-jwt @types/serve-favicon

# Initialize TailwindCSS
npx tailwindcss init

# Set the scripts
node -e "let pkg=require('./package.json'); pkg.scripts={start: 'ts-node app.ts', 'build-css': 'postcss public/css/tailwind.css -o public/css/styles.css'}; require('fs').writeFileSync('package.json', JSON.stringify(pkg, null, 2));"

# Set the project as private
node -e "let pkg=require('./package.json'); pkg.private=true; require('fs').writeFileSync('package.json', JSON.stringify(pkg, null, 2));"

# Create the TypeScript configuration file
cat << EOF > tsconfig.json
{
    "compilerOptions": {
        "target": "es6",
        "module": "commonjs",
        "strict": true,
        "esModuleInterop": true,
        "skipLibCheck": true,
        "forceConsistentCasingInFileNames": true
    },
    "include": ["**/*.ts"],
    "exclude": ["node_modules"]
}
EOF

# Create a basic app.ts file
cat << EOF > app.ts
import express from 'express';
import path from 'path';
import favicon from 'serve-favicon';

const app = express();
const port = 3000;

// Set the view engine to ejs
app.set('view engine', 'ejs');

app.use(express.static('public'));

// Middleware to serve the favicon
app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));

// Serve the main page
app.get('/', (req, res) => {
    res.render('index', { title: 'Simple Backend', message: 'Welcome to Simple Backend!' });
});


app.listen(port, () => {
  console.log(\`Server is running at http://localhost:\${port}\`);
});
EOF

# Create a basic .env file
cat << EOF > .env
CLIENT_ID=test-client
CLIENT_SECRET=YOUR_SECRET
EOF

# Create tailwindcss config
cat << EOF > tailwind.config.js
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [],
  theme: {
    extend: {},
  },
  plugins: [],
  purge: [
    './views/**/*.ejs',
  ],
}
EOF

cat << EOF > public/css/tailwind.css
/* tailwind.css */

@import 'tailwindcss/base';
@import 'tailwindcss/components';
@import 'tailwindcss/utilities';

/* Global Styles */
body {
  font-family: Arial, sans-serif;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

/* Main Page Styles */
.main-page {
  @apply bg-gray-200;
}

.main-page .content {
  @apply p-8 text-center;
}

.main-page h1 {
  @apply text-3xl font-bold text-gray-700 mb-4;
}

.main-page p {
  @apply text-gray-700 mb-6;
}

.main-page .btn {
  @apply inline-block bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded;
}

/* Protected Page Styles */
.protected-page {
  @apply bg-gray-200;
}

.protected-page .content {
  @apply p-8 text-center;
}

.protected-page h1 {
  @apply text-3xl font-bold text-gray-700 mb-4;
}

.protected-page .token-container {
  @apply mb-6 bg-white rounded p-4;
}

.protected-page .token-text {
  @apply w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring focus:ring-blue-500 resize-none;
  height: 150px;
}

.protected-page .token-info {
  @apply text-left mb-4 text-gray-700;
}

.protected-page .token-info strong {
  @apply font-bold;
}

.protected-page .back-link {
  @apply text-blue-500 underline;
}

.protected-page .btn {
  display: inline-block;
  border-radius: 0.25rem;
  --tw-bg-opacity: 1;
  background-color: rgb(59 130 246 / var(--tw-bg-opacity));
  padding-top: 0.5rem;
  padding-bottom: 0.5rem;
  padding-left: 1rem;
  padding-right: 1rem;
  font-weight: 700;
  --tw-text-opacity: 1;
  color: rgb(255 255 255 / var(--tw-text-opacity));
}

.protected-page .btn:hover {
  --tw-bg-opacity: 1;
  background-color: rgb(29 78 216 / var(--tw-bg-opacity));
}
EOF

# Generate tailwind css
npm run build-css

# Create index.ejs
cat << EOF > views/index.ejs
<!-- views/index.ejs -->

<!DOCTYPE html>
<html>
  <head>
    <title>Test Page</title>
    <link href="/css/styles.css" rel="stylesheet" />
  </head>
  <body class="main-page">
    <div class="container">
      <div class="content">
        <h1>Hello World</h1>
      </div>
    </div>
  </body>
</html>
EOF

# Create .gitignore
cat << EOF > .gitignore
node_modules
EOF

echo "Project setup completed in directory: $dir"
