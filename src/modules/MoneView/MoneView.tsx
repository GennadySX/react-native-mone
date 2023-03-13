/*
 * @author GennadySX
 * @created at 2023
 **/

import React, { useCallback, useEffect, useMemo, useRef } from 'react';
import {
  findNodeHandle,
  ImageRequireSource,
  PixelRatio,
  Platform,
  UIManager,
  Image,
  ImageURISource,
} from 'react-native';
import MoneViewModule, {
  MODULE_NAME,
  ResizeImageProps,
} from './ResizeImageView';
import { MoneFilters } from '../MoneFilter/MoneFilter';

export const IS_ANDROID = Platform.OS === 'android';
const getPlatformRatios = (val: number) =>
  IS_ANDROID ? PixelRatio.getPixelSizeForLayoutSize(val) : val;

type MoneViewProps = Omit<ResizeImageProps, 'ref' | 'source'> & {
  source: ImageRequireSource | ImageURISource;
  width: number;
  height: number;
  filter?: MoneFilters;
};
export const MoneView = (props: MoneViewProps) => {
  const ref = useRef(null);

  const fixedWidth = props?.width;
  const fixedHeight = props?.height;
  const isCustomSize = !!(fixedWidth && fixedHeight);
  const commandIs =
    UIManager.getViewManagerConfig(MODULE_NAME)?.Commands?.create?.toString();

  const createFragment = useCallback(
    (viewId: number | null) => {
      if (!viewId) {
        return;
      }
      UIManager.dispatchViewManagerCommand(viewId, commandIs, [viewId]);
    },
    [commandIs]
  );

  const fixedSizes = useMemo<
    Pick<ResizeImageProps['source'], 'width' | 'height'>
  >(
    () => ({
      width: getPlatformRatios(fixedWidth),
      height: getPlatformRatios(fixedHeight),
    }),
    [fixedHeight, fixedWidth]
  );

  const source = useMemo(() => {
    const uri =
      typeof props.source === 'number'
        ? Image.resolveAssetSource(props.source).uri
        : props.source.uri;
    return {
      ...fixedSizes,
      uri,
      filter: props.filter,
    };
  }, [fixedSizes, props.filter, props.source]);

  const isCrash = useMemo(() => {
    return !(source?.width && source?.height && source?.uri && source?.filter);
  }, [source?.filter, source?.height, source?.uri, source?.width]);

  useEffect(() => {
    if (IS_ANDROID) {
      setTimeout(() => {
        createFragment(findNodeHandle(ref.current));
      }, 200);
    }
  }, [createFragment]);

  if (isCrash) {
    return null;
  }

  return (
    <MoneViewModule
      ref={ref}
      source={source}
      style={[props.style, isCustomSize && fixedSizes]}
    />
  );
};
