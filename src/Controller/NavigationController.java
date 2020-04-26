package Controller;

import View.View;

import java.util.ArrayList;
import java.util.List;

public class NavigationController implements Controller {
        private final List<View> views;

        public NavigationController() {
            this.views = new ArrayList<View>();
        }

        public void addView(View view) {
            this.views.add(view);
        }

        public void removeView(View view) {
            this.views.remove(view);
        }

        @Override
        public List<String> getOptions() {
            List<String> options = new ArrayList<String>();
            for(View view: this.views)
                options.add(view.getTitle());

            return options;
        }

        @Override
        public void onOptionSelected(View view, int option) {
            this.views.get(option).show();
        }

}

