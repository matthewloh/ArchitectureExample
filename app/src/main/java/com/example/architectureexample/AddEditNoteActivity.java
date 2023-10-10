package com.example.architectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.architectureexample.databinding.ActivityAddNoteBinding;

import java.util.Objects;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.architectureexample.EXTRA_ID";
    private ActivityAddNoteBinding binding;
    // Constant keys for IntentExtras
    // IntentExtras allow us to pass data between Activities using someActivityResultLauncher
    public static final String EXTRA_TITLE = "com.example.architectureexample.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.architectureexample.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.architectureexample.EXTRA_PRIORITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.numberPickerPriority.setMinValue(1);
        binding.numberPickerPriority.setMaxValue(10);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        // If the intent has an extra with the key EXTRA_ID, then we know that we are editing an existing note
        // and we can set the title and description accordingly
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            binding.editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            binding.editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            binding.numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else {
            setTitle("Add Note");
        }

        setTitle("Add Note");
    }

    private void saveNote() {
        String title = binding.editTextTitle.getText().toString();
        String description = binding.editTextDescription.getText().toString();
        int priority = binding.numberPickerPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }
        // We need another intent to pass the data back to the MainActivity
        // Which receives an intent in the onActivityResult() method
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        // If the intent has an extra with the key EXTRA_ID, then we know that we are editing an existing note
        // and we can set the title and description accordingly
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        // A result code of RESULT_OK indicates that the Activity succeeded, which in the onActivityResult() method override
        // we can use to check if the Activity succeeded or not, with the resultCode parameter corresponding to a request code
        setResult(RESULT_OK, data);
        finish();
    }

    // Called to inflate the menu resource
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    // For our one menu item, we can just check the ID of the item and call the saveNote() method if it matches
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_note) {
            saveNote();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}