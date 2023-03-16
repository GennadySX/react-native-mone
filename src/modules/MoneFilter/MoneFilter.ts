/*
 * @author GennadySX
 * @created at 2023
 **/

import { NativeModules } from 'react-native';

const MoneFilterModule = NativeModules.MoneFilter as {
  filterImage: (
    path: string,
    filter: MoneFilters,
    success: (base64: string) => void
  ) => void;
  getFilters: () => Promise<string[]>;
};

const filterImage = (
  path: string,
  filter: MoneFilters,
  success: (base64: string) => void
) => MoneFilterModule.filterImage(path, filter, success);

export enum MoneFilters {
  sepia = 'SEPIA',
  grayscale = 'GRAYSCALE',
  invert = 'INVERT',
  blur = 'BLUR',
  sharpen = 'SHARPEN',
  threshold = 'THRESHOLD',
  blackAndWhite = 'BLACK_AND_WHITE',
  none = 'NONE',
}

export default {
  filterImage,
  MoneFilterModule,
};
