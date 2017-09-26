# cp-select
Select box component for Angular 2

## Install

```shell
npm i -S @bromzh/ng2-select
```

## Setup

```js
import { SelectModule } from '@bromzh/ng2-select';

@NgModule({
    imports: [ SelectModule ],
})
export class AppModule { }
```

## Usage

```html
<br-select [multiple]="true" [(ngModel)]="selected" name="select">
    <br-select-option [value]="2" [text]="'Option 2'">Option 2</br-select-option>
    <br-select-option [value]="3" [text]="'Option 3'">Option 3</br-select-option>
    <br-select-option [value]="1" [text]="'Option 1'">Option 1</br-select-option>
</br-select>
```
