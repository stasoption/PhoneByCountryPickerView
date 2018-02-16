# PhoneByCountryPickerView for Android

## Demo

![alt tag](https://media.giphy.com/media/3003DyRvjOqRSNq6Jh/giphy.gif)

When first start, PhoneByCountryPickerView automatically initialized default phone locale. Also, you can pick other country and add some custom parameters such as background, text color, icons etc.

![alt tag](https://media.giphy.com/media/i4dwZjwUT68szW5Tl3/giphy.gif)

![alt tag](https://media.giphy.com/media/2tKbmvoldOWjrpIAWW/giphy.gif)

## Usage

**In your XML file**<br />

```
    <com.stasoption.countrypicker.View.CountryPickerView
        android:id="@+id/countryPickerView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```

**In your activity**<br />

```
        CountryPickerView countryPickerView = findViewById(R.id.countryPickerView);
        countryPickerView.setOnGettingPhoneNumberListener(new CountryPickerView.OnPhoneNumberPickListener() {
            /*called before the user going to select a country from the countries list...*/
            @Override
            public void onUserStartPickCountry(){
                mPhoneNumber.setText("");
                mCountry.setText("");
            }

            /**
             * called when the user selected a country from the countries list
             * @param phone number without special symbols such as "+", "-" " " etc
             * @param countryCode code of the country in ISO format (RU, US...)
             */
            @Override
            public void onUserPickedCountry(String phone, String countryCode) {
                mMainLayout.requestFocus();
                mCountry.setText(getCountry(countryCode));
                mPhoneNumber.setText(phone);
            }
        });
```

**Custom parameters**<br />

```
    <com.stasoption.countrypicker.View.CountryPickerView
        android:id="@+id/countryPickerView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:backgroundDrawableCountryPicker="@drawable/bg_phone_picker"
        app:colorValidButtonCountryPicker="#FFEB3B"
        app:colorInvalidButtonCountryPicker="#F44336"
        app:iconValidButtonCountryPicker="@drawable/ic_forward_swg"
        app:iconInvalidButtonCountryPicker="@drawable/ic_clear_swg"
        app:textColorCountryPicker="#FFEB3B"
        app:textSizeCountryPicker="18"
        app:paddingCountryPicker="16"/>
```    

## How to add

**Gradle**<br />

```

      dependencies {
           compile 'com.github.stasoption:countrypickerview:1.0.1' 
      }
      
```

**Maven**<br />

```

    <dependency>
        <groupId>com.github.stasoption</groupId>
        <artifactId>countrypickerview</artifactId>
        <version>1.0.1</version>
        <type>pom</type>
     </dependency>

```

## License

      The MIT License (MIT)
      Copyright (c) 2017 Stas Averin

      Permission is hereby granted, free of charge, to any person obtaining a copy
      of this software and associated documentation files (the "Software"), to deal
      in the Software without restriction, including without limitation the rights
      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
      copies of the Software, and to permit persons to whom the Software is
      furnished to do so, subject to the following conditions:

      The above copyright notice and this permission notice shall be included in all
      copies or substantial portions of the Software.

      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
      SOFTWARE.

