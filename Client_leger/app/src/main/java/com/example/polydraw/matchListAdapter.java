package com.example.polydraw;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polydraw.Socket.SocketIO;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class matchListAdapter extends RecyclerView.Adapter<matchListAdapter.MyViewHolder> {
    private ArrayList<String> matchList;
    private String token;
    private String username;
    private String lastName;
    private String firstName;
    private String _id;
    private SocketIO socket;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public Button button;

        public MyViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.gameName);
            button = (Button) view.findViewById(R.id.joinButton);
        }

    }

    public matchListAdapter(ArrayList<String> matchList, String token, String username, String lastName, String firstName, String _id, SocketIO socket) {
        this.matchList = matchList;
        this.token = token;
        this.username = username;
        this.lastName = lastName;
        this.firstName = firstName;
        this._id = _id;
        this.socket = socket;
    }

    @Override public int getItemCount() {
        return matchList.size();
    }

    @Override public matchListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);

        return new matchListAdapter.MyViewHolder(itemView);
    }

    @Override public void onBindViewHolder(final matchListAdapter.MyViewHolder holder, final int position){
        final String s = matchList.get(position);
        holder.name.setText(s);
        System.out.println();
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WaitingRoom.class);
                intent.putExtra("matchId", s);
                intent.putExtra("token", token);
                intent.putExtra("username", username);
                intent.putExtra("firstName", firstName);
                intent.putExtra("lastName", lastName);
                intent.putExtra("_id", _id);
//                v.getContext().getString();
                System.out.print("PRINT USERNAME POUR EMIT JOIN: "+s +" "+ username);
                socket.getSocket().emit("joinGame", s, username);
                /*Map<String, String> postData = new HashMap<>();
                postData.put("player", username);
//                postData.put("score", "0");
//                postData.put("_id", _id);
                JSONObject jsonObject = new JSONObject(postData);
                HttpPatch task = new HttpPatch(postData);
                task.execute(SocketIO.HTTP_URL+"match/"+s+"player/");*/
                v.getContext().startActivity(intent);

            }
        });

    }

    public class HttpPatch extends AsyncTask<String, Void, String> {
        JSONObject postData;
        public String token;
        public JSONObject player;
        public String playerString;
        public String playerName;
        public String playerLastName;
        public String playerUsername;
        private String result;

        public HttpPatch(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
                System.out.println(this.postData);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println(result);


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);


                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("PATCH");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);

                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    System.out.println("PATCH DATA "+postData.toString());
                    writer.write(postData.toString());
                    writer.flush();
                }

                final int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200 || statusCode == 201) {
                    System.out.println(statusCode + ": Successful request");
//                    status = statusCode;
                    if (statusCode < 299) { // success
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        String data = response.toString();
                        JSONObject reader = new JSONObject(data);
                        player = reader.getJSONObject("player");
                        playerString = player.toString();
                        result = playerString;

                        token = player.get("token").toString();
                        playerUsername = player.get("username").toString();
                        playerLastName = player.get("lastName").toString();
                        playerName = player.get("firstName").toString();

                    }
                } else {
                    System.out.println(statusCode + ": Something went wrong...");
                    result = null;
                }


            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }
    }
}
