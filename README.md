# PagerIndicator
# 自定义tablayout


        tabLayout.setAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public ITabView getTabView(Context context, int index) {
                return null;
            }

            @Override
            public ITabIndicator getIndicator(Context context) {
                return null;
            }
        });
![enter image description here](https://github.com/zhaotong/PagerIndicator/blob/master/image/device-2019-04-17-154128.png)
![enter image description here](https://github.com/zhaotong/PagerIndicator/blob/master/image/device-2019-04-17-154205.png)

