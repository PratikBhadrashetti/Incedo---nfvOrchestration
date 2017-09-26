import {
    Component, OnInit, Input, HostBinding, ElementRef,
    style, transition, state, trigger, animate, ChangeDetectionStrategy, forwardRef,
} from '@angular/core';
import { SelectOptionComponent } from './select-option.component';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

const CP_SELECT_VALUE_ACCESSOR = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => SelectComponent),
    multi: true,
};

@Component({
    selector: 'br-select',
    providers: [ CP_SELECT_VALUE_ACCESSOR ],
    host: {
        '(document:click)': 'onClick($event)',
    },
    template: `
        <div class="cp-select" [class.focus]="focused">
            <div class="cp-select-input-group" >
                <input type="text" class="cp-select-input"
                    autocomplete="off"
                    [readonly]="!optionsVisible"
                    [id]="id"
                    [placeholder]="getPlaceholder()"
                    [disabled]="disabled"
                    [tabIndex]="tabIndex"
                    [attr.name]="name"
                    [attr.aria-label]="ariaLabel"
                    [attr.aria-labelledby]="ariaLabelledby"
                    [ngModel]="getSelectText()"
                    (ngModelChange)="onSearchQueryChanged($event)"
                    (focus)="onFocus($event)"
                    (blur)="onBlur($event)"
                    [hidden]="hidden"
                    #viewInput>
                <div class="cp-select-arrow" (click)="onArrowClick()" *ngIf="!hidden">
                    <svg:svg viewBox="0 0 10 10" preserveAspectRatio="xMidYMid" width="10" height="10">
                        <g class="cp-select-arrow" [@arrow]="optionsVisible ? 'up' : 'down'">
                            <path d="m 1 3.5 l 8 0 l -4 4 z" fill="#000000"/>
                        </g>
                    </svg:svg>
                </div>
            </div>
            <div class="cp-select-options-container"
                [@options]="optionsVisible ? 'visible' : 'hidden'">
                <div class="cp-select-options">
                    <ng-content select="br-select-option"></ng-content>
                </div>
            </div>
        </div>
    `,
    styles: [`
        :host {
            display: inline-block;
            /*width: 100%;*/
            position: relative;
        }
        .cp-select.focus > .cp-select-input-group {
            box-shadow: 0 0 2px rgba(0,0,0,0.5);
        }
        .cp-select.focus > .cp-select-options-container {
            box-shadow: 0 0 2px rgba(0,0,0,0.5);
        }
        .cp-select-options-container {
            overflow-y: auto;
            position: absolute;
            width: 100%;
            background-color: #CCCCCC;
            z-index: 1000;
            max-height : 155px;
            
        }
        .cp-select-input-group {
            display: flex;

        }
        .cp-select-input {
            width: 100%;
            padding: 0.5rem;
            flex: 1;
            flex-basis: initial;
            border: 1px solid #CCCCCC;
            border-top-left-radius:4px;
            border-bottom-left-radius:4px;
            border-right: 0;
            padding:5px 10px;
        }
        .cp-select-input:focus {
            outline: none;
        }
        .cp-select-input:active {
            outline: none;
        }
        .cp-select-arrow {
            flex: 0;
            border: 1px solid #CCCCCC;
            border-top-right-radius:4px;
            border-bottom-right-radius:4px;
            border-left: 0;
            transform-origin: 50% 50%;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            padding: 0 5px;
        }
        .cp-select-options {
            overflow-y: auto;
            display: flex;
            flex-direction: column;
            border-left: 1px solid #CCCCCC;
            border-right: 1px solid #CCCCCC;
        }
    `],
    animations: [
        trigger('arrow', [
            state('down', style({ transform: 'rotate(0deg)' })),
            state('up', style({ transform: 'rotate(180deg)' })),
            transition('down <=> up', animate('250ms ease')),
        ]),
        trigger('options', [
            state('visible', style({ height: '*' })),
            state('hidden', style({ height: 0 })),
            transition('hidden => visible', [
                style({ visibility: 'visible' }),
                animate('250ms ease'),
            ]),
            transition('visible => hidden', [
                animate('250ms ease', style({ height: 0 })),
                style({ visibility: 'hidden' }),
            ]),
        ]),
    ],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SelectComponent implements ControlValueAccessor {
    @Input() id: string;
    @Input() placeholder: string;
    @Input() disabled: boolean;
    @Input() name: string;
    @Input() tabIndex: number = 0;
    @Input() ariaLabel: string;
    @Input() ariaLabelledby: string;
    @Input() hidden: boolean;

    @Input() multiple: boolean = true;

    @HostBinding('class.focus') // TODO change to 'focus'
    focused: boolean;

    @HostBinding('class.open')
    optionsVisible: boolean;

    options: SelectOptionComponent[] = [];
    selectedItem: SelectOptionComponent;
    searchQuery: string = '';
    value: any;

    constructor(private elRef: ElementRef) { }

    @Input()
    identifyFn: (a: any, b: any) => boolean = (a: any, b: any) => a === b;

    onChange = (_: any) => {};
    onTouched = () => {};

    writeValue(obj: any): void {
        this.value = obj;
        this.options.forEach(e => e.selected = false);
        if (obj instanceof Array) {
            (obj as Array<any>).forEach(e => {
                this.options.filter(o => this.identifyFn(o.value, e)).forEach(o => o.selected = true);
            });
        } else {
            this.options.filter(o => this.identifyFn(o.value, obj)).forEach(o => {
                o.selected = true;
                this.selectedItem = o;
            });
        }
    }

    registerOnChange(fn: any): void {
        this.onChange = fn;
    }

    registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    onFocus(event: FocusEvent) {
        this.showOptions();
        this.focused = true;
    }

    onBlur(event: FocusEvent) {
        // this.optionsVisible = false;
    }

    onClick(event: Event) {
        if (!this.elRef.nativeElement.contains(event.target)) {
            this.hideOptions();
            this.focused = false;
        }
    }

    getPlaceholder() {
        if (this.optionsVisible) {
            if (!this.searchQuery || this.searchQuery === '') {
                return this.getViewText();
            }
            return '';
        }
        return this.placeholder || '';
    }

    getSelectText() {
        if (this.optionsVisible) {
            return this.searchQuery || '';
        }
        return this.getViewText();
    }

    getViewText() {
        if (this.multiple) {
            return this.getSelectedOptions().map(o => o.text).join(', ');
        }
        return this.selectedItem ? this.selectedItem.text : '';
    }

    onSearchQueryChanged(query: string) {
        this.options.forEach(o => o.hidden = !o.text.includes(query));
        this.searchQuery = query;
    }

    onArrowClick() {
        this.toggleOptions();
    }

    showOptions() {
        if (this.searchQuery !== '') {
            this.onSearchQueryChanged('');
            this.searchQuery = '';
        }
        // setTimeout(() => this.viewInput.nativeElement.focus(), 0);
        this.optionsVisible = true;
        this.onTouched();
    }

    hideOptions() {
        this.optionsVisible = false;
        if (this.focused) {
            // setTimeout(() => this.viewInput.nativeElement.focus(), 0);
        }
    }

    toggleOptions() {
        this.optionsVisible ? this.hideOptions() : this.showOptions();
    }

    addOption(option: SelectOptionComponent) {
        this.options.push(option);
        this.writeValue(this.value);
    }

    removeOption(option: SelectOptionComponent) {
        this.options = this.options.filter(o => o.value !== option.value);
        this.writeValue(this.value);
    }

    onOptionClick(option: SelectOptionComponent) {
        if (this.multiple) {
            option.selected = !option.selected;
            this.onChange(this.getSelectedValues());
            return;
        }
        option.selected = true;
        this.options
            .filter(o => !this.identifyFn(o.value, option.value))
            .forEach(o => o.selected = false);
        this.selectedItem = option;
        this.onChange(option.value);
        this.hideOptions();
    }

    getSelectedOptions(): Array<SelectOptionComponent> {
        return this.options.filter(o => o.selected);
    }

    getSelectedValues(): Array<SelectOptionComponent> {
        return this.getSelectedOptions().map(o => o.value);
    }
}
