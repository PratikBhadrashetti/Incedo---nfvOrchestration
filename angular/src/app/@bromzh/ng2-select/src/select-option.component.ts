import {
    Component, OnInit, HostListener, Host, HostBinding, Input,
    style, transition, state, trigger, animate, OnDestroy,
} from '@angular/core';
import { SelectComponent } from './select.component';

@Component({
    selector: 'br-select-option',
    host: {
        '[@option]': 'hidden ? "hidden" : "visible"',
    },
    template: `
        <div class="cp-select-option">
            <br-check *ngIf="select.multiple" [checked]="selected" class="cp-select-option-check"></br-check>
            <div class="cp-select-option-content"><ng-content></ng-content></div>
        </div>
    `,
    styles: [`
        :host {
            cursor: pointer;
            background-color: #fff;
            overflow: hidden;
            border-bottom: 1px solid #CCCCCC;
            
        }
        :host:hover {
            background-color: #eee;
        }
        .cp-select-option {
            display: inline-flex;
            align-items: center;
            padding: 0.5rem;
        }
        .cp-select-option-check {
            flex: 0;
            flex-basis: initial;
            margin-right: 0.5rem;
            /*padding: 0 0.5rem;*/
        }
        .cp-select-option-content {
            flex: 1;
        }
    `],
    animations: [
        trigger('option', [
            state('hidden', style({ opacity: 0, height: 0, display: 'none' })),
            state('visible', style({ opacity: '*', height: '*', display: '*' })),
            transition('visible <=> hidden', animate('250ms ease')),
        ]),
    ],
})
export class SelectOptionComponent implements OnInit, OnDestroy {
    @Input() value: any;
    @Input() text: string;

    @HostBinding('class.selected')
    selected: boolean;

    @HostBinding('class.hidden')
    hidden: boolean;

    constructor(@Host() protected select: SelectComponent) {}

    ngOnInit(): void {
        this.select.addOption(this);
    }

    ngOnDestroy(): void {
        this.select.removeOption(this);
    }

    @HostListener('click', ['$event'])
    onClick(event: MouseEvent) {
        event.preventDefault();
        this.select.onOptionClick(this);
    }
}
