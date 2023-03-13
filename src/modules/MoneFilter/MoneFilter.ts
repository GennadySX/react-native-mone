/*
 * @author GennadySX
 * @created at 2023
 **/

import { NativeModules } from 'react-native';

const MoneFilterModule = NativeModules.MoneFilter as {
  filterImage: (path: string, filter: string) => Promise<string>;
  getFilters: () => Promise<string[]>;
};

const filterImage = (path: string, filter: string) => {
  return MoneFilterModule.filterImage(path, filter);
};
const supportFilters = MoneFilterModule.getFilters().then((filters) => filters);

export enum MoneFilters {
  sepia = 'SEPIA',
  grayscale = 'GRAYSCALE',
  invert = 'INVERT',
  blur = 'BLUR',
  blackAndWhite = 'BLACK_AND_WHITE',
  none = 'NONE',
}

export default {
  filterImage,
  supportFilters,
  MoneFilterModule,
};
