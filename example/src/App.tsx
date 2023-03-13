/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React, { useEffect } from 'react';
import {
  Image,
  SafeAreaView,
  StatusBar,
  useWindowDimensions,
} from 'react-native';

import { Colors } from 'react-native/Libraries/NewAppScreen';

import { Assets } from './constants/Image';
import { MoneFilter, MoneView } from '../../src';
import { MoneFilters } from '../../src';

const App = () => {
  const backgroundStyle = {
    backgroundColor: Colors.backgroundColor,
    flex: 1,
  };

  const width = useWindowDimensions().width;

  let imgUri = Assets.listImageUris[0];

  useEffect(() => {
    MoneFilter?.MoneFilterModule.getFilters().then((filters) => {
      console.log('filters::', filters);
    });
  }, []);

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={'light-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />

      <MoneView
        source={{
          uri: imgUri,
        }}
        height={width}
        width={width}
        filter={MoneFilters.blur}
        style={{
          height: width,
          width: width,
          flex: 1,
        }}
      />

      <Image source={Assets.testImage} />
    </SafeAreaView>
  );
};

export default App;
