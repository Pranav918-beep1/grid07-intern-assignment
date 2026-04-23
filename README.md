# Grid07 Intern Assignment

## Overview
This project implements a backend system simulating social media interactions between users and bots. It focuses on controlling bot activity, maintaining data integrity under concurrent requests, and tracking engagement through a Redis-based virality scoring system.

## Tech Stack
- Java (Spring Boot)
- PostgreSQL
- Redis
- Docker

## Running the Application

### 1. Start dependencies
```bash
docker-compose up -d
2. Run the application
mvn spring-boot:run

The service will start on port 8080.

API Endpoints
Create Post
POST /api/posts
Add Comment
POST /api/posts/{postId}/comments
Like Post
POST /api/posts/{postId}/like
Get Virality Score
GET /api/debug/virality/{postId}
Core Features
Virality Scoring

Virality is maintained in Redis and updated based on interactions:

Like: +20
Human comment: +50
Bot reply: +1
Bot Interaction Controls
Maximum of 100 bot replies per post
Maximum comment depth of 20
10-minute cooldown between bot replies to the same user

Bot reply limits are enforced using a Redis Lua script to ensure atomicity under concurrent load.

Notification System
15-minute notification cooldown per user
Pending notifications stored in Redis
Periodic aggregation via scheduled job
Notes
Redis is used for all transient and high-frequency operations.
PostgreSQL is used for persistent storage.
The system avoids in-memory state to remain stateless and scalable.
