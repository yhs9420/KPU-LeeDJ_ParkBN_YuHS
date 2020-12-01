package org.tensorflow.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.demo.R;
import org.tensorflow.demo.list.Recipe;

import java.util.List;

/**
 * Created by kch on 2018. 2. 17..
 */

public class RecipeListAdapter extends BaseAdapter {

    private Context context;
    private List<Recipe> recipeList;
    private List<Recipe> saveList;

    public RecipeListAdapter(Context context, List<Recipe> recipeList, List<Recipe> saveList) {
        this.context = context;
        this.recipeList = recipeList;
        this.saveList = saveList;

    }


    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return recipeList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return recipeList.get(i);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }

    //가장 중요한 부분
    //int i 에서 final int i 로 바뀜 이유는 deleteButton.setOnClickListener에서 이 값을 참조하기 때문
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.recipe, null);

        //뷰에 다음 컴포넌트들을 연결시켜줌
        //final추가 안붙이면 에러남 리스너로 전달하고 싶은 지역변수는 final로 처리해야됨
        TextView recipe_name = (TextView) v.findViewById(R.id.recipe_name);
        TextView recipe_category = (TextView) v.findViewById(R.id.recipe_category);
        //TextView recipe_ingredient = (TextView)v.findViewById(R.id.recipe_ingredient);
        //TextView recipe_content = (TextView)v.findViewById(R.id.recipe_content);
        ImageView recipeImage = (ImageView) v.findViewById((R.id.recipeImage));

        recipe_name.setText(recipeList.get(i).getRecipe_name());
        recipe_category.setText(recipeList.get(i).getRecipe_category());
        //recipe_ingredient.setText(recipeList.get(i).getRecipe_ingredient());
        //recipe_content.setText(recipeList.get(i).getRecipe_content());

        String rCategory = recipe_category.getText().toString();

        if ("한식".equals(rCategory))
            recipeImage.setImageResource(R.drawable.korean);
        else if ("중식".equals(rCategory))
            recipeImage.setImageResource(R.drawable.chinese);
        else if ("일식".equals(rCategory))
            recipeImage.setImageResource(R.drawable.japanese);
        else if ("양식".equals(rCategory))
            recipeImage.setImageResource(R.drawable.western);

        /*Intent intent = new Intent(v.getContext(), TestActivity.class);
        String userID = intent.getStringExtra("userID");
        Button detailButton = (Button)v.findViewById(R.id.detailButton);

        detailButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RecipeClickedActivity.class);
                intent.putExtra("user_id", userID);
                intent.putExtra("recipe_id", recipeList.get(i).getRecipe_id());
                intent.putExtra("recipe_name", recipeList.get(i).getRecipe_name());
                intent.putExtra("recipe_category", recipeList.get(i).getRecipe_category());
                intent.putExtra("recipe_ingredient", recipeList.get(i).getRecipe_ingredient());
                intent.putExtra("recipe_content", recipeList.get(i).getRecipe_content());
                context.startActivity(intent);

            }});
*/
        //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임
        v.setTag(recipeList.get(i).getRecipe_id());
        //만든뷰를 반환함
        return v;
    }

}

