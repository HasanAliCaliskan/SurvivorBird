package com.hasanali.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {

	SpriteBatch batch;
	Texture backround;
	Texture bird;
	Texture enemy1;
	Texture enemy2;
	Texture enemy3;
	BitmapFont font;
	BitmapFont font2;
	Random random;
	Circle birdCircle;
	Circle[] enemyCircles;
	Circle[] enemyCircles2;
	Circle[] enemyCircles3;
	int gameState = 0;
	int score = 0;
	int scoredEnemy = 0;
	int numberOfEnemies = 4;
	float birdX = 0;
	float birdY = 0;
	float birdVelocity = 0;
	float enemyVelocity = 12;
	float gravity = 0.5f;
	float[] enemyX = new float[numberOfEnemies];
	float[] enemyOffSet = new float[numberOfEnemies];
	float[] enemyOffSet2 = new float[numberOfEnemies];
	float[] enemyOffSet3 = new float[numberOfEnemies];
	float distance = 0;

	@Override
	public void create () {
		initialize();
		setVariables();
	}

	public void initialize () {
		batch = new SpriteBatch();
		backround = new Texture("sky_background.png");
		bird = new Texture("bird.png");
		enemy1 = new Texture("enemy.png");
		enemy2 = new Texture("enemy.png");
		enemy3 = new Texture("enemy.png");
		font = new BitmapFont();
		font2 = new BitmapFont();
		random = new Random();
		birdCircle = new Circle();
		enemyCircles = new Circle[numberOfEnemies];
		enemyCircles2 = new Circle[numberOfEnemies];
		enemyCircles3 = new Circle[numberOfEnemies];
	}

	public void setVariables () {
		birdX = (Gdx.graphics.getWidth() / 2) - (bird.getWidth() / 2);
		birdY = Gdx.graphics.getHeight() / 2;
		distance = Gdx.graphics.getWidth() / 2;

		font.setColor(Color.WHITE);
		font.getData().setScale(4);
		font2.setColor(Color.WHITE);
		font2.getData().setScale(5);

		setEnemys();
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(backround,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		scoreControl();

		if (gameState == 1) {
			gameState_1();

		} else if(gameState == 0) {
			justTouched(0);

		} else if(gameState == 2) {
			font2.draw(batch,"Game Over! Your Score: " + String.valueOf(score),100,9*Gdx.graphics.getHeight() / 10);
			justTouched(2);
		}

		batch.draw(bird, birdX, birdY,Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
		font.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
		batch.end();
		birdCircle.set(birdX + (Gdx.graphics.getWidth()/30), birdY + (Gdx.graphics.getHeight()/20), Gdx.graphics.getWidth()/30);
	}

	public void gameState_1 () {
		justTouched(1);
		if (birdY > 0) {
			birdVelocity += gravity;
			birdY -= birdVelocity;
		} else {
			gameState = 2;
		}

		for (int i = 0; i<numberOfEnemies; i++) {
			if (enemyX[i] < 0) {
				enemyX[i] += numberOfEnemies * distance;
				enemyOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
				enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
				enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			} else {
				enemyX[i] -= enemyVelocity;
			}

			batch.draw(enemy1,enemyX[i],(Gdx.graphics.getHeight() / 2) + enemyOffSet[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
			batch.draw(enemy2,enemyX[i],(Gdx.graphics.getHeight() / 2) + enemyOffSet2[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
			batch.draw(enemy3,enemyX[i],(Gdx.graphics.getHeight() / 2) + enemyOffSet3[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
			enemyCircles[i].set(enemyX[i] + (Gdx.graphics.getWidth()/30),enemyOffSet[i] + (Gdx.graphics.getHeight() / 2) + (Gdx.graphics.getHeight()/20),Gdx.graphics.getWidth()/30);
			enemyCircles2[i].set(enemyX[i] + (Gdx.graphics.getWidth()/30),enemyOffSet2[i] + (Gdx.graphics.getHeight() / 2) + (Gdx.graphics.getHeight()/20),Gdx.graphics.getWidth()/30);
			enemyCircles3[i].set(enemyX[i] + (Gdx.graphics.getWidth()/30),enemyOffSet3[i] + (Gdx.graphics.getHeight() / 2) + (Gdx.graphics.getHeight()/20),Gdx.graphics.getWidth()/30);
		}
	}

	public void setEnemys () {
		for (int i = 0; i < numberOfEnemies; i++) {
			enemyX[i] = Gdx.graphics.getWidth() - enemy1.getWidth() + i * distance;

			enemyOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

			enemyCircles[i] = new Circle();
			enemyCircles2[i] = new Circle();
			enemyCircles3[i] = new Circle();
		}
	}

	public void scoreControl () {
		if (enemyX[scoredEnemy] < Gdx.graphics.getWidth() / 2 - bird.getHeight() / 2) {
			score++;
			if (scoredEnemy < numberOfEnemies - 1) {
				scoredEnemy++;
			} else {
				scoredEnemy = 0;
			}
		}
		for (int i = 0; i<numberOfEnemies; i++) {
			if(Intersector.overlaps(birdCircle, enemyCircles[i]) || Intersector.overlaps(birdCircle, enemyCircles2[i]) || Intersector.overlaps(birdCircle, enemyCircles3[i])
			|| birdY > Gdx.graphics.getHeight()) {
				gameState = 2;
			}
		}
	}

	public void justTouched (int gameStateControl) {
		if (Gdx.input.justTouched()) {
			if (gameStateControl == 0) {
				gameState = 1;
			} else if (gameStateControl == 1) {
				birdVelocity = -15;
			} else {
				birdVelocity = 0;
				scoredEnemy = 0;
				score = 0;
				gameState = 1;
				birdY = Gdx.graphics.getHeight() / 2;
				setEnemys();
			}
		}
	}

	@Override
	public void dispose () {
	}
}