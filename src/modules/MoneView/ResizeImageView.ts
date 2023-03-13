/*
 * @author GennadySX
 * @created at 2023
 **/
import { ImageStyle, requireNativeComponent, StyleProp } from 'react-native';
import { MoneFilters } from '../MoneFilter/MoneFilter';
import React from 'react';

export const MODULE_NAME = 'MoneView';

export type ResizeImageProps = {
  source: {
    uri: string;
    filter?: MoneFilters;
    width: number;
    height: number;
  };
  style?: StyleProp<ImageStyle>;
  ref?: React.RefObject<any>;
};

const MoneViewModule = requireNativeComponent(
  MODULE_NAME
) as React.ComponentType<ResizeImageProps>;

export default MoneViewModule;
