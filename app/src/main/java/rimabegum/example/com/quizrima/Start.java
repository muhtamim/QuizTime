package rimabegum.example.com.quizrima;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import rimabegum.example.com.quizrima.Common.Common;
import rimabegum.example.com.quizrima.Model.Question;

import java.util.Collections;

public class Start extends AppCompatActivity {

    Button btnPlay;

    FirebaseDatabase database;
    DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        database= FirebaseDatabase.getInstance();
        questions=database.getReference("Questions");
        
        loadQuestion(Common.categoryId);

        btnPlay=(Button)findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Start.this,Playing.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadQuestion(String categoryId) {

        //First, clear list if have old questions
        if(Common.questionList.size() > 0)
        {
            Common.questionList.clear();
        }

        questions.orderByChild("CategoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Question ques=postSnapshot.getValue(Question.class);
                    Common.questionList.add(ques);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Random list
        Collections.shuffle(Common.questionList);
    }
}
