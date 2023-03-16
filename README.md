# react-native-mone

React Native image filters and effects library.

## Installation

```sh
npm install react-native-mone
```

## Usage

```js
import { MoneView, MoneFilters } from "react-native-mone";

// ...

const source = {
  uri: "https://i.imgur.com/0Z0Z0Z0.jpg",
  width: 100,
  height: 100,
};

// ...



<MoneView
  source={source}
  width={100}
  height={100}
  filter={MoneFilters.sepia}
  style={
    borderRadius: 50
  }

/>
```


## Usage as Module

```ts
import { MoneFilters } from './MoneFilter';

const imgUri: string = "..."
const filter: MoneFilters = MoneFilters.grayscale

const onSetFilter = () =>
  ImageFilter.filterImage(
    imgUri,
    filter, //as enum from MoneFilters
    (base64: string) => {
      const imgBase64 = `data:image/png;base64,${res}`;
      console.log('imgBase64:: -> ', imgBase64);
    },
  );

```


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with ❤️ by [GennadySX](https://github.com/GennadySX)
